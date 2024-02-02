import "../../styles/App.css"
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.bundle.min"
import Form from "react-bootstrap/Form"
import { useTranslation } from "react-i18next"
import { useState } from "react"

function EnterpriseSortBySelect() {
  const { t } = useTranslation()

  const [sortBy, setSortBy] = useState("")

  return (
    <div style={{ width: "200px" }}>
      <Form.Select
        className="px-3"
        aria-label="Sort by select"
        value={sortBy}
        onChange={(e) => setSortBy(e.target.value)}
      >
        <option value="0"> {t("Order By")} </option>
        <option value="1"> {t("Job Offer")} </option>
        <option value="2"> {t("Name")} </option>
        <option value="4"> {t("Date asc")} </option>
        <option value="5"> {t("Date desc")} </option>
      </Form.Select>
    </div>
  )
}

export default EnterpriseSortBySelect
