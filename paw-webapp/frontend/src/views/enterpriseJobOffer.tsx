import Button from "react-bootstrap/Button"
import Header from "../components/header"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import { useTranslation } from "react-i18next"
import { useEffect } from "react"

function JobOfferEnterprise() {
  const { t } = useTranslation()

  useEffect(() => {
    document.title = t("Job Offer Page Title")
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
                  <strong>Add job offers to your enterprise</strong>
                </h2>
                <p>Make sure to fill all fields before advancing.</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Form className="msform">
                      <div className="form-card">
                        <h2 className="fs-title">Job Offer</h2>
                        <Form.Group className="mb-3" controlId="formBasicPosition">
                          <Form.Control className="input" placeholder="Position *" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicSalary">
                          <Form.Control className="input" placeholder="Salary" />
                        </Form.Group>
                        <div className="d-flex mb-4">
                          <label className="area">Modality</label>
                          <Form.Select className="selectFrom" aria-label="Default select example">
                            <option value="Remoto">Home Office</option>
                            <option value="Presencial">On site</option>
                            <option value="Mixto">Mixed</option>
                          </Form.Select>
                        </div>
                        {/* TODO: agregar HABILIDADES REQUERIDAS CON EL INPUT DE +*/}
                        <div className="d-flex mb-4">
                          <label className="area">Job Category</label>
                          <Form.Select className="selectFrom" aria-label="Default select example">
                            {/* TODO: agregar for con las categories pasadas de la API*/}
                            <option value="category">Category Name</option>
                          </Form.Select>
                        </div>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                          <Form.Control placeholder="Description" as="textarea" rows={3} />
                        </Form.Group>
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

export default JobOfferEnterprise
