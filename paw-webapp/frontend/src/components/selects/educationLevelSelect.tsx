import "../../styles/App.css"
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.bundle.min"
import Form from "react-bootstrap/Form"
import { useTranslation } from "react-i18next"
import { useState, useEffect } from "react"

function EducationLevelSelect() {
  const { t } = useTranslation()

  const [educationLevel, setEducationLevel] = useState("")

  return (
    <div>
      <Form.Select
        className="px-3"
        aria-label="Education Level select"
        value={educationLevel}
        onChange={(e) => setEducationLevel(e.target.value)}
      >
        <option key="1" value="">
          {t("Education Level")}
        </option>
        <option value="Primario"> {t("Primario")} </option>
        <option value="Secundario"> {t("Secundario")} </option>
        <option value="Terciario"> {t("Terciario")} </option>
        <option value="Graduado"> {t("Graduado")} </option>
        <option value="Postgrado"> {t("Postgrado")} </option>
      </Form.Select>
    </div>
  )
}

export default EducationLevelSelect
