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

package controllers

import connectors.CalculationConnector
import controllers.actions.{DataRetrievalAction, IdentifierAction}
import models.{Calculation, NormalMode}
import pages.SalaryPage
import play.api.Logging
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.ResultViewModel
import views.html.ResultView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ResultController  @Inject()(
                                   override val messagesApi: MessagesApi,
                                   identify: IdentifierAction,
                                   getData: DataRetrievalAction,
                                   val controllerComponents: MessagesControllerComponents,
                                   view: ResultView,
                                   sessionRepository: SessionRepository,
                                   calculationConnector: CalculationConnector
                                 )(implicit ec: ExecutionContext)
  extends FrontendBaseController with I18nSupport with Logging {

  def onPageLoad: Action[AnyContent] = (identify andThen getData).async {
    implicit request =>

      request.userAnswers.flatMap { answers =>
        answers.get(SalaryPage).map(Calculation(_))
          .map {
            calculation =>
              val viewModel = ResultViewModel(calculation)

              calculationConnector.submit(calculation).map { _ =>
                Ok(view(viewModel))
              }.recover { e: Throwable =>
                logger.error(s"Failed to send calculation to backend: ${e.getMessage}")
                Ok(view(viewModel))
              }
          }
      }.getOrElse {
        logger.warn("User has no user answers, redirecting to the guidance page")
        Future.successful(Redirect(routes.IndexController.onPageLoad))
      }
  }

  def onSubmit: Action[AnyContent] = identify.async {
    implicit request =>

      sessionRepository.clear(request.userId).map { _ =>
        Redirect(routes.SalaryController.onPageLoad(NormalMode))
      }
  }
}
