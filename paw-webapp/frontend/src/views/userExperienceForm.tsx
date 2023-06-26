import Button from "react-bootstrap/Button"
import Header from "../components/header"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import { useTranslation } from "react-i18next"
import { useEffect } from "react"

function ExperienceForm() {
  const { t } = useTranslation()

  useEffect(() => {
    document.title = t("Experience Form Page Title")
  }, [])

  return (
    <div>
      <Header />
      <div className="d-flex justify-content-between mt-2">
        <Container>
          <div className="row justify-content-center mt-0">
            <div className="col-11 col-sm-9 col-md-7 col-lg-6 p-0 mt-3 mb-2">
              <Card className="custom-card px-0 pt-4 pb-0 mt-3 mb-3">
                <h2 className="text-center p-0 mt-3 mb-2">
                  <strong>Add experiences to your profile</strong>
                </h2>
                <p>Make sure to fill all fields before advancing.</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Form className="msform">
                      <div className="form-card">
                        <h2 className="fs-title">Experience</h2>
                        <Form.Group className="mb-3" controlId="formBasicEnterprise">
                          <Form.Control className="input" placeholder="Enterprise *" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicPosition">
                          <Form.Control className="input" placeholder="Position *" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicDescription">
                          <Form.Control className="input" placeholder="Description" />
                        </Form.Group>
                        <div className="d-flex mb-4">
                          <div className="row ml-4">
                            <div className="col-sm-4">
                              <label>From *</label>
                            </div>
                            <div className="col-sm-4">
                              <Form.Select className="selectFrom">
                                <option value="Enero">January</option>
                                <option value="Febrero">February</option>
                                <option value="Marzo">March</option>
                                <option value="Abril">April</option>
                                <option value="Mayo">May</option>
                                <option value="Junio">June</option>
                                <option value="Julio">July</option>
                                <option value="Agosto">August</option>
                                <option value="Septiembre">September</option>
                                <option value="Octubre">October</option>
                                <option value="Noviembre">November</option>
                                <option value="Diciembre">December</option>
                              </Form.Select>
                            </div>
                            <div className="col-sm-4">
                              <Form.Control className="input" placeholder="YYYY" />
                            </div>
                          </div>
                        </div>
                        <div className="d-flex mb-4">
                          <div className="row ml-4">
                            <div className="col-sm-4">
                              <label>To</label>
                            </div>
                            <div className="col-sm-4">
                              <Form.Select className="selectTo">
                                <option value="No-Especificado">Choose</option>
                                <option value="Enero">January</option>
                                <option value="Febrero">February</option>
                                <option value="Marzo">March</option>
                                <option value="Abril">April</option>
                                <option value="Mayo">May</option>
                                <option value="Junio">June</option>
                                <option value="Julio">July</option>
                                <option value="Agosto">August</option>
                                <option value="Septiembre">September</option>
                                <option value="Octubre">October</option>
                                <option value="Noviembre">November</option>
                                <option value="Diciembre">December</option>
                              </Form.Select>
                            </div>
                            <div className="col-sm-4">
                              <Form.Control className="input" placeholder="YYYY" />
                            </div>
                          </div>
                        </div>
                      </div>
                      <p>(*) Fields are required.</p>
                      {/* TODO: arreglar el metodo de link porque href es ilegal - funciona though*/}
                      <Button href="/" variant="success" type="submit">
                        <strong>Save</strong>
                      </Button>
                    </Form>
                    <div className="row">
                      <div className="col mt-2 mb-2">
                        <Button href="/" variant="outline-secondary">
                          <strong>return</strong>
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

export default ExperienceForm
