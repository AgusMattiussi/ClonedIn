import Button from "react-bootstrap/Button"
import Header from "../components/header"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"

function ContactForm() {
  return (
    <div>
      <Header />
      <div className="d-flex justify-content-between mt-2">
        <Container>
          <div className="row justify-content-center mt-0">
            <div className="col-11 col-sm-9 col-md-7 col-lg-6 p-0 mt-3 mb-2">
              <Card className="custom-card px-0 pt-4 pb-0 mt-3 mb-3">
                <h2 className="text-center p-0 mt-3 mb-2">
                  <strong>Send message to USERNAME</strong>
                </h2>
                <p>Make sure to fill all fields before advancing.</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Form className="msform">
                      <div className="form-card">
                        <h2 className="fs-title">Message</h2>
                        <Form.Group className="mb-3" controlId="formBasicMessage">
                          <Form.Control className="input" placeholder="We would like you as a candidate" />
                        </Form.Group>
                        <div className="d-flex mb-4">
                          <label className="area">Job Offers *</label>
                          <Form.Select className="selectFrom" aria-label="Default select example">
                            {/* TODO: agregar for con las job offers pasadas de la API*/}
                            <option value="job offer id">Job Offer Name</option>
                          </Form.Select>
                        </div>
                      </div>
                      <p>(*) Fields are required.</p>
                      {/* TODO: arreglar el metodo de link porque href es ilegal - funciona though*/}
                      <Button href="/" variant="success" type="submit">
                        <strong>Register</strong>
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

export default ContactForm
