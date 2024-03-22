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

import models.OverallResult.{LessNiDue, MinimalDifference, NoDifference}
import play.api.libs.functional.syntax._
import play.api.libs.json._

import scala.math.BigDecimal.RoundingMode.HALF_DOWN

sealed trait OverallResult

object OverallResult {
  case object NoDifference extends OverallResult
  case object MinimalDifference extends OverallResult
  case object LessNiDue extends OverallResult
}

final case class Calculation private(
                                      annualSalary: BigDecimal,
                                      dec23EstimatedNic: BigDecimal,
                                      mar24EstimatedNic: BigDecimal,
                                      apr24EstimatedNic: BigDecimal
                                    ) {

  lazy val mar24Apr24MonthlySaving: BigDecimal =
    (mar24EstimatedNic - apr24EstimatedNic).setScale(0, HALF_DOWN)

  lazy val dec23Apr24MonthlySaving: BigDecimal =
    (dec23EstimatedNic - apr24EstimatedNic).setScale(0, HALF_DOWN)

  lazy val mar24Apr24OverallResult: OverallResult =
    if (mar24EstimatedNic == apr24EstimatedNic) NoDifference
    else if (mar24Apr24MonthlySaving == 0)      MinimalDifference
    else                                        LessNiDue

  lazy val dec23Apr24OverallResult: OverallResult = {
    if (dec23EstimatedNic == apr24EstimatedNic) NoDifference
    else if (dec23Apr24MonthlySaving == 0) MinimalDifference
    else LessNiDue
  }

  lazy val dec23Apr24AnnualSaving: BigDecimal =
    ((dec23EstimatedNic - apr24EstimatedNic) * 12).setScale(0, HALF_DOWN)

  lazy val mar24Apr24AnnualSaving: BigDecimal =
    ((mar24EstimatedNic - apr24EstimatedNic) * 12).setScale(0, HALF_DOWN)
}

object Calculation {

  def apply(annualSalary: BigDecimal): Calculation = {

    val threshold     = BigDecimal(12570)
    val upperLimit    = BigDecimal(50270)
    val dec23MainRate = BigDecimal(0.12)
    val mar24MainRate = BigDecimal(0.10)
    val apr24MainRate = BigDecimal(0.08)
    val upperRate     = BigDecimal(0.02)

    val primarySalary = (annualSalary - threshold).min(upperLimit - threshold).max(0)
    val upperSalary   = (annualSalary - upperLimit).max(0)

    val dec23EstimatedNic = (primarySalary * dec23MainRate / 12).setScale(2, HALF_DOWN) + (upperSalary * upperRate / 12).setScale(2, HALF_DOWN)
    val mar24EstimatedNic = (primarySalary * mar24MainRate / 12).setScale(2, HALF_DOWN) + (upperSalary * upperRate / 12).setScale(2, HALF_DOWN)
    val apr24EstimatedNic = (primarySalary * apr24MainRate / 12).setScale(2, HALF_DOWN) + (upperSalary * upperRate / 12).setScale(2, HALF_DOWN)

    Calculation(annualSalary, dec23EstimatedNic, mar24EstimatedNic, apr24EstimatedNic)
  }

  implicit lazy val writes: OWrites[Calculation] = (
    (__ \ "annualSalary").write[BigDecimal] and
    (__ \ "dec23EstimatedNic").write[BigDecimal] and
    (__ \ "mar24EstimatedNic").write[BigDecimal] and
    (__ \ "apr24EstimatedNic").write[BigDecimal] and
    (__ \ "dec23Apr24AnnualSaving").write[BigDecimal] and
    (__ \ "mar24Apr24AnnualSaving").write[BigDecimal]
  )(c => (c.annualSalary, c.dec23EstimatedNic, c.mar24EstimatedNic, c.apr24EstimatedNic, c.dec23Apr24AnnualSaving, c.mar24Apr24AnnualSaving))
}