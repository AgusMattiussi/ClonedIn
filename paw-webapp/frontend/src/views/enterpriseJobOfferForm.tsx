import Button from "react-bootstrap/Button"
import Navigation from "../components/navbar"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router-dom"

function JobOfferForm() {
  const { t } = useTranslation()
  const navigate = useNavigate()

  document.title = t("Job Offer Page Title")

  return (
    <div>
      <Navigation />
      <div className="d-flex justify-content-between mt-2">
        <Container>
          <div className="row justify-content-center mt-0">
            <div className="col-11 col-sm-9 col-md-7 col-lg-6 p-0 mt-3 mb-2">
              <Card className="custom-card px-0 pt-4 pb-0 mt-3 mb-3">
                <h2 className="text-center p-0 mt-3 mb-2">
                  <strong> {t("JobOffer add")} </strong>
                </h2>
                <p>{t("Fill all fields")}</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Form className="msform">
                      <div className="form-card">
                        <h2 className="fs-title"> {t("Job Offer")} </h2>
                        <Form.Group className="mb-3 mt-3" controlId="formBasicPosition">
                          <Form.Control className="input" placeholder={t("Position").toString()} />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicSalary">
                          <Form.Control className="input" placeholder={t("Salary").toString()} />
                        </Form.Group>
                        <div className="d-flex mb-4">
                          <label className="area"> {t("Modality")} </label>
                          <Form.Select className="selectFrom" aria-label="Default select example">
                            <option value="Remoto">{t("Home Office")}</option>
                            <option value="Presencial">{t("On site")}</option>
                            <option value="Mixto">{t("Mixed")}</option>
                          </Form.Select>
                        </div>
                        {/* TODO: agregar HABILIDADES REQUERIDAS CON EL INPUT DE +*/}
                        <div className="d-flex mb-4">
                          <label className="area">{t("Job Category")}</label>
                          <Form.Select className="selectFrom" aria-label="Default select example">
                            {/* TODO: agregar for con las categories pasadas de la API*/}
                            <option value="category">Category Name</option>
                          </Form.Select>
                        </div>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                          <Form.Control placeholder={t("Description").toString()} as="textarea" rows={3} />
                        </Form.Group>
                      </div>
                      <p>{t("Fields required")}</p>
                      <Button onClick={() => navigate(-1)} variant="success" type="submit">
                        <strong> {t("Create")} </strong>
                      </Button>
                    </Form>
                    <div className="row">
                      <div className="col mt-2 mb-2">
                        <Button onClick={() => navigate(-1)} variant="outline-secondary">
                          <strong>{t("Return")}</strong>
                        </Button>
                      </div>
                    </div>
                  </div>
                </div>
              </Card>
            </div>
          </div>
        </Container>
      </div>
    </div>
  )
}

export default JobOfferForm
