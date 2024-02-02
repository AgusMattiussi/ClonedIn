import "../../styles/App.css"
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.bundle.min"
import Form from "react-bootstrap/Form"
import { useTranslation } from "react-i18next"
import { useState } from "react"

function ModalitySelect() {
  const { t } = useTranslation()

  const [modality, setModality] = useState("")

  return (
    <div>
      <Form.Select
        className="px-3"
        aria-label="Categories select"
        value={modality}
        onChange={(e) => setModality(e.target.value)}
      >
        <option key="1" value="">
          {t("Modality")}
        </option>
        <option value="Remoto">{t("Home Office")}</option>
        <option value="Presencial">{t("On Site")}</option>
        <option value="Mixto">{t("Mixed")}</option>
      </Form.Select>
    </div>
  )
}

export default ModalitySelect
