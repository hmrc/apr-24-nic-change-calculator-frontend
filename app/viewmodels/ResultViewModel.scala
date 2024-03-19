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

package viewmodels

import config.CurrencyFormatter.currencyFormat
import models.Calculation
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Key
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.{SummaryList, SummaryListRow}
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

import java.time.format.DateTimeFormatter

case class ResultViewModel(calculation: Calculation)(implicit messages: Messages) {

  private val dec23Apr24Rows: List[SummaryListRow] = List(
    SummaryListRowViewModel(
      key     = s"result.annualSalary",
      value   = ValueViewModel(currencyFormat(calculation.annualSalary)),
      actions = Nil
    ),
    SummaryListRowViewModel(
      key     = s"result.dec23EstimatedNic",
      value   = ValueViewModel(currencyFormat(calculation.dec23EstimatedNic)),
      actions = Nil
    ),
    SummaryListRowViewModel(
      key     = s"result.apr24EstimatedNic",
      value   = ValueViewModel(currencyFormat(calculation.apr24EstimatedNic)),
      actions = Nil
    ),
    SummaryListRowViewModel(
      key     = s"result.dec23Apr24AnnualSaving",
      value   = ValueViewModel(currencyFormat(calculation.dec23Apr24AnnualSaving)),
      actions = Nil
    )
  )

  private val mar24Apr24Rows: List[SummaryListRow] = List(
    SummaryListRowViewModel(
      key     = s"result.annualSalary",
      value   = ValueViewModel(currencyFormat(calculation.annualSalary)),
      actions = Nil
    ),
    SummaryListRowViewModel(
      key     = s"result.mar24EstimatedNic",
      value   = ValueViewModel(currencyFormat(calculation.mar24EstimatedNic)),
      actions = Nil
    ),
    SummaryListRowViewModel(
      key     = s"result.apr24EstimatedNic",
      value   = ValueViewModel(currencyFormat(calculation.apr24EstimatedNic)),
      actions = Nil
    ),
    SummaryListRowViewModel(
      key     = s"result.mar24Apr24AnnualSaving",
      value   = ValueViewModel(currencyFormat(calculation.mar24Apr24AnnualSaving)),
      actions = Nil
    )
  )

  val dec23Apr24SummaryList: SummaryList =
    SummaryList(dec23Apr24Rows).withCssClass("govuk-summary-list--long-key")

  val mar24Apr24SummaryList: SummaryList =
    SummaryList(mar24Apr24Rows).withCssClass("govuk-summary-list--long-key")
}

