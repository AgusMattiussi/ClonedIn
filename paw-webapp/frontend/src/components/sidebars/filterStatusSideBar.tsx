import "../../styles/App.css"
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.bundle.min"
import Button from "react-bootstrap/Button"
import Col from "react-bootstrap/esm/Col"
import { useTranslation } from "react-i18next"

function FilterStatusSideBar() {
  const { t } = useTranslation()

  return (
    <Col sm={2} className="sidebar">
      <div className="d-flex flex-column justify-content-center">
        <div className="search mx-auto">
          <h5 className="ml-2 mt-2">{t("Filter by status")}:</h5>
        </div>
        <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
          <Button variant="outline-light " className="filterbtn">
            {t("Accepted")}
          </Button>
        </div>
        <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
          <Button variant="outline-light " className="filterbtn">
            {t("Rejected")}
          </Button>
        </div>
        <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
          <Button variant="outline-light " className="filterbtn">
            {t("Pending")}
          </Button>
        </div>
        <div className="d-flex flex-wrap justify-content-center mt-2 mx-4">
          <Button variant="outline-light " className="filterbtn">
            {t("Cancelled")}
          </Button>
        </div>
        <div className="d-flex flex-wrap justify-content-center mt-4 mx-auto" style={{ maxWidth: "fit-content" }}>
          <Button variant="outline-light " className="filterbtn">
            {t("View All")}
          </Button>
        </div>
      </div>
    </Col>
  )
}

export default FilterStatusSideBar
