import "../../styles/App.css"
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.bundle.min"
import Button from "react-bootstrap/Button"
import * as Icon from "react-bootstrap-icons"
import Form from "react-bootstrap/Form"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import CategoriesSelect from "../selects/categoriesSelect"
import ModalitySelect from "../selects/modalitySelect"
import { useTranslation } from "react-i18next"

function FilterJobsSideBar() {
  const { t } = useTranslation()

  return (
    <Col sm={2} className="sidebar">
      <Row className="search">
        <h5 className="ml-2 mt-2">{t("Search By")}</h5>
      </Row>
      <Row>
        <Form className="search">
          <Form.Control
            type="search"
            placeholder={t("Search Job Offer Placeholder").toString()}
            className="me-2"
            aria-label="Search"
          />
          <div className="d-flex flex-wrap justify-content-center mt-2">
            <Button variant="outline-light" className="filterbtn" type="submit">
              <Icon.Search size={15} />
            </Button>
          </div>
        </Form>
      </Row>
      <br />
      <Row className="search">
        <h5>{t("Filter By")}</h5>
      </Row>
      <Row>
        <CategoriesSelect />
      </Row>
      <br />
      <Row>
        <ModalitySelect />
      </Row>
      <Row className="search mt-2">
        <div className="d-flex justify-content-center">
          <h6>{t("Salary")}</h6>
        </div>
      </Row>
      <Row className="search">
        <Form className="d-flex">
          <Form.Control type="search" placeholder={t("Minimum").toString()} className="me-2" aria-label="Search" />
          -
          <Form.Control type="search" placeholder={t("Maximum").toString()} className="ms-2" aria-label="Search" />
        </Form>
        <br />
        <div className="d-flex flex-wrap justify-content-center mt-2">
          <Button variant="outline-light " className="filterbtn">
            {t("Filter")}
          </Button>
        </div>
      </Row>
      <br />
      <Row>
        <div className="d-flex flex-wrap justify-content-center">
          <Button variant="outline-light " className="filterbtn">
            {t("Clear Filter")}
          </Button>
        </div>
      </Row>
    </Col>
  )
}

export default FilterJobsSideBar
