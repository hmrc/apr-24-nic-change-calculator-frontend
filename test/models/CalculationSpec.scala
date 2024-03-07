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

import org.scalacheck.Gen
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class CalculationSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks {

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
}
