/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import org.scalacheck.{Gen, Shrink}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class CalculationSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks {

  implicit val dontShrink: Shrink[String] = Shrink.shrinkAny

  ".dec23EstimatedNic" - {

    "must be 0 when the salary is £12,570 or less (below threshold)" in {

      forAll(Gen.choose[BigDecimal](1, 12570)) { salary =>
        Calculation(salary).dec23EstimatedNic mustEqual 0
      }
    }

    "must be 0 when the salary is £12,570.50 (above threshold, rounding down)" in {

      Calculation(BigDecimal(12570.5)).dec23EstimatedNic mustEqual 0
    }

    "must be £0.01 when the salary is £12,570.51 (above threshold, rounding up)" in {

      Calculation(BigDecimal(12570.51)).dec23EstimatedNic mustEqual BigDecimal(0.01)
    }

    "must be £377 when the salary as £50,270 (at upper earnings limit)" in {

      Calculation(BigDecimal(50270)).dec23EstimatedNic mustEqual BigDecimal(377)
    }

    "must be £377 when the salary is £50,273 (above upper earnings limit, rounding down)" in {

      Calculation(BigDecimal(50273)).dec23EstimatedNic mustEqual BigDecimal(377)
    }

    "must be £377.01 when the salary is £50,273.01 (above upper earnings limit, rounding up)" in {

      Calculation(BigDecimal(50273.01)).dec23EstimatedNic mustEqual BigDecimal(377.01)
    }

    "must be £378 when the salary is £50,870 (NICs accrue at 2% for income above the upper earnings limit)" in {

      Calculation(BigDecimal(50870)).dec23EstimatedNic mustEqual BigDecimal(378)
    }
  }

  ".mar24EstimatedNic" - {

    "must be 0 when the salary is £12,570 or less (below threshold)" in {

      forAll(Gen.choose[BigDecimal](1, 12570)) { salary =>
        Calculation(salary).mar24EstimatedNic mustEqual 0
      }
    }

    "must be 0 when the salary is £12,570.60 (above threshold, rounding down)" in {

      Calculation(BigDecimal(12570.60)).mar24EstimatedNic mustEqual 0
    }

    "must be £0.01 when the salary is £12,570.61 (above threshold, rounding up)" in {

      Calculation(BigDecimal(12570.61)).mar24EstimatedNic mustEqual BigDecimal(0.01)
    }

    "must be £314.17 when the salary as £50,270 (at upper earnings limit)" in {

      Calculation(BigDecimal(50270)).mar24EstimatedNic mustEqual BigDecimal(314.17)
    }

    "must be £314.17 when the salary is £50,273 (above upper earnings limit, rounding down)" in {

      Calculation(BigDecimal(50273)).mar24EstimatedNic mustEqual BigDecimal(314.17)
    }

    "must be £314.18 when the salary is £50,273.01 (above upper earnings limit, rounding up)" in {

      Calculation(BigDecimal(50273.01)).mar24EstimatedNic mustEqual BigDecimal(314.18)
    }

    "must be £315.17 when the salary is £50,870 (NICs accrue at 2% for income above the upper earnings limit)" in {

      Calculation(BigDecimal(50870)).mar24EstimatedNic mustEqual BigDecimal(315.17)
    }
  }

  ".apr24EstimatedNic" - {

    "must be 0 when the salary is £12,570 or less (below threshold)" in {

      forAll(Gen.choose[BigDecimal](1, 12570)) { salary =>
        Calculation(salary).apr24EstimatedNic mustEqual 0
      }
    }

    "must be 0 when the salary is £12,570.75 (above threshold, rounding down)" in {

      Calculation(BigDecimal(12570.75)).apr24EstimatedNic mustEqual 0
    }

    "must be £0.01 when the salary is £12,570.76 (above threshold, rounding up)" in {

      Calculation(BigDecimal(12570.76)).apr24EstimatedNic mustEqual BigDecimal(0.01)
    }

    "must be £251.33 when the salary as £50,270 (at upper earnings limit)" in {

      Calculation(BigDecimal(50270)).apr24EstimatedNic mustEqual BigDecimal(251.33)
    }

    "must be £251.33 when the salary is £50,273 (above upper earnings limit, rounding down)" in {

      Calculation(BigDecimal(50273)).apr24EstimatedNic mustEqual BigDecimal(251.33)
    }

    "must be £251.34 when the salary is £50,273.01 (above upper earnings limit, rounding up)" in {

      Calculation(BigDecimal(50273.01)).apr24EstimatedNic mustEqual BigDecimal(251.34)
    }

    "must be £252.33 when the salary is £50,870 (NICs accrue at 2% for income above the upper earnings limit)" in {

      Calculation(BigDecimal(50870)).apr24EstimatedNic mustEqual BigDecimal(252.33)
    }
  }

  ".mar24Apr24MonthlySaving" - {

    "must be zero when the difference between Mar 24 and Apr 24 estimates is £0.50 or less (rounding down)" in {

      val calculation = Calculation(BigDecimal(12874))

      calculation.mar24EstimatedNic - calculation.apr24EstimatedNic mustEqual BigDecimal(0.5)
      calculation.mar24Apr24MonthlySaving mustEqual 0
    }

    "must be £1 when the difference between Mar 24 and Apr 24 estimates is £0.51 (rounding up)" in {

      val calculation = Calculation(BigDecimal(12875))

      calculation.mar24EstimatedNic - calculation.apr24EstimatedNic mustEqual BigDecimal(0.51)
      calculation.mar24Apr24MonthlySaving mustEqual BigDecimal(1)
    }

    "must be £63 when the salary is £50,270 or above (above upper earnings limit, attracting the maximum saving)" in {

      forAll(Gen.choose[BigDecimal](50270, 1000000)) { salary =>
        Calculation(salary).mar24Apr24MonthlySaving mustEqual BigDecimal(63)
      }
    }
  }

  ".dec24Apr24MonthlySaving" - {

    "must be zero when the difference between Dec 23 and Apr 24 estimates is £0.50 or less (rounding down)" in {

      val calculation = Calculation(BigDecimal(12720))

      calculation.dec23EstimatedNic - calculation.apr24EstimatedNic mustEqual BigDecimal(0.5)
      calculation.dec23Apr24MonthlySaving mustEqual 0
    }

    "must be £1 when the difference between Dec 23 and Apr 24 estimates is £0.51 (rounding up)" in {

      val calculation = Calculation(BigDecimal(12722))

      calculation.dec23EstimatedNic - calculation.apr24EstimatedNic mustEqual BigDecimal(0.51)
      calculation.dec23Apr24MonthlySaving mustEqual BigDecimal(1)
    }

    "must be £126 when the salary is £50,270 or above (above upper earnings limit, attracting the maximum saving)" in {

      forAll(Gen.choose[BigDecimal](50270, 1000000)) { salary =>
        Calculation(salary).dec23Apr24MonthlySaving mustEqual BigDecimal(126)
      }
    }
  }

  ".dec23Apr24OverallResult" - {

    "must be No Difference when the estimated NICs for Dec 23 and Apr 24 are the same" in {

      forAll(Gen.choose[BigDecimal](1, BigDecimal(12570))) { salary =>

        Calculation(salary).dec23Apr24OverallResult mustEqual OverallResult.NoDifference
      }
    }

    "must be Minimal Difference when the estimated NICs for Dec 23 are up to 50p higher than for Apr 24" in {

      forAll(Gen.choose(BigDecimal(12572), BigDecimal(12721))) { salary =>

        Calculation(salary).dec23Apr24OverallResult mustEqual OverallResult.MinimalDifference
      }
    }

    "must be LessNiDue when the estimated NICs for Dec 23 are more than 50p higher than for Apr 24" in {

      forAll(Gen.choose(BigDecimal(12722), BigDecimal(1000000))) { salary =>

        Calculation(salary).dec23Apr24OverallResult mustEqual OverallResult.LessNiDue
      }
    }
  }

  ".mar24Apr24OverallResult" - {

    "must be No Difference when the estimated NICs for Dec 23 and Apr 24 are the same" in {

      forAll(Gen.choose[BigDecimal](1, BigDecimal(12570))) { salary =>

        Calculation(salary).mar24Apr24OverallResult mustEqual OverallResult.NoDifference
      }
    }

    "must be Minimal Difference when the estimated NICs for Dec 23 are up to 50p higher than for Apr 24" in {

      forAll(Gen.choose(BigDecimal(12575), BigDecimal(12871))) { salary =>

        Calculation(salary).mar24Apr24OverallResult mustEqual OverallResult.MinimalDifference
      }
    }

    "must be LessNiDue when the estimated NICs for Dec 23 are more than 50p higher than for Apr 24" in {

      forAll(Gen.choose(BigDecimal(128756), BigDecimal(1000000))) { salary =>

        Calculation(salary).mar24Apr24OverallResult mustEqual OverallResult.LessNiDue
      }
    }
  }
}
