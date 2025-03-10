import Spinner from "react-bootstrap/Spinner"
import { useTranslation } from "react-i18next"

function Loader() {
  const { t } = useTranslation()

  return (
    <Spinner animation="border" role="status">
      <span className="visually-hidden">{t("Loading")}</span>
    </Spinner>
  )
}

export default Loader
