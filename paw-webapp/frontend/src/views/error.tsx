import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router-dom"
import { Button } from "react-bootstrap"
import { HttpStatusCode } from "axios"
import { useSharedAuth } from "../api/auth"

function Error(props: any) {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const { userInfo } = useSharedAuth()

  let errorCode = ""
  let title = ""
  let message = ""

  switch (props.statusCode) {
    case HttpStatusCode.Unauthorized:
      errorCode = "401"
      title = t("401 Title")
      message = t("401 Msg")
      break
    case HttpStatusCode.Forbidden:
      errorCode = "403"
      title = t("403 Title")
      message = t("403 Msg")
      break
    case HttpStatusCode.NotFound:
      errorCode = "404"
      title = t("404 Title")
      message = t("404 Msg")
      break
    default:
      errorCode = "500"
      title = t("500 Title")
      message = t("500 Msg")
      break
  }

  document.title = "Error | ClonedIn"

  return (
    <div className="d-flex align-items-center justify-content-center vh-100">
      <div className="text-center">
        <h1 className="display-1 fw-bold">{errorCode}</h1>
        <p className="fs-3">
          <span className="text-danger">{t("Error Title Span")}</span> {title}
        </p>
        <p className="lead">{message}</p>
        {userInfo?.role === "USER" ? (
          <Button onClick={() => navigate("/jobs")} className="btn btn-primary" style={{ backgroundColor: "#04704C" }}>
            {t("Return")}
          </Button>
        ) : (
          <Button
            onClick={() => navigate("/profiles")}
            className="btn btn-primary"
            style={{ backgroundColor: "#04704C" }}
          >
            {t("Return")}
          </Button>
        )}
      </div>
    </div>
  )
}

export default Error
