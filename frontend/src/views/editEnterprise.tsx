import Button from "react-bootstrap/Button"
import Header from "../components/header"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"

function EditEnterprise() {
  return (
    <div>
      <Header />
      <div className="d-flex justify-content-between mt-2">
        <Container>
          <div className="row justify-content-center mt-0">
            <div className="col-11 col-sm-9 col-md-7 col-lg-6 p-0 mt-3 mb-2">
              <Card className="custom-card px-0 pt-4 pb-0 mt-3 mb-3">
                <h2 className="text-center p-0 mt-3 mb-2">
                  <strong>Edit your profile</strong>
                </h2>
                <p>Make sure to fill all fields before advancing.</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Form className="msform">
                      <div className="form-card">
                        <h2 className="fs-title">Basic Information</h2>
                        <Form.Group className="mb-3" controlId="formBasicName">
                          <Form.Control className="input" placeholder="Enterprise Name" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicLocation">
                          <Form.Control className="input" placeholder="Location" />
                        </Form.Group>
                        <div className="d-flex mb-4">
                          <label className="area">Quantity of employees</label>
                          <Form.Select className="selectFrom" aria-label="Default select example">
                            <option value="No-especificado">Choose</option>
                            <option value="1-10">1-10</option>
                            <option value="11-50">11-50</option>
                            <option value="51-100">51-100</option>
                            <option value="101-200">101-200</option>
                            <option value="201-500">201-500</option>
                            <option value="501-1000">501-1000</option>
                            <option value="1001-5000">1001-5000</option>
                            <option value="5001-10000">5001-10000</option>
                            <option value="10000+">10001+</option>
                          </Form.Select>
                        </div>
                        <div className="d-flex mb-4">
                          <label className="area">Job Category</label>
                          <Form.Select className="selectFrom" aria-label="Default select example">
                            {/* TODO: agregar for con las categories pasadas de la API*/}
                            <option value="category">Category Name</option>
                          </Form.Select>
                        </div>
                        <Form.Group className="mb-3" controlId="formBasicYear">
                          <Form.Control className="input" placeholder="Funding Year" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicWebsite">
                          <Form.Control className="input" placeholder="Website" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                          <Form.Control placeholder="About Us" as="textarea" rows={3} />
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

export default EditEnterprise
