import Button from 'react-bootstrap/Button';
import * as Icon from 'react-bootstrap-icons';
import Header from './header';
import Container from 'react-bootstrap/esm/Container';
import Form from 'react-bootstrap/Form';
import Card from 'react-bootstrap/Card';

function RegisterUser() {
  
  return (
    <div>
    <Header/>
    <div className="d-flex justify-content-between mt-2">
        <Container>
        <div className="row justify-content-center mt-0">
            <div className="col-11 col-sm-9 col-md-7 col-lg-6 p-0 mt-3 mb-2">
            <Card className="custom-card px-0 pt-4 pb-0 mt-3 mb-3">
            <h2 className="text-center p-0 mt-3 mb-2"><strong>Register</strong></h2>
            <p>Make sure to fill all fields before advancing.</p>
            <div className="row">
                        <div className="col-md-12 mx-0">
    <Form className='msform'>
        <div className="form-card">
        <h2 className="fs-title">Basic Information</h2>
      <Form.Group className="mb-3" controlId="formBasicEmail">
        <Form.Control className='input' type="email" placeholder="Email *" />
      </Form.Group>
      <Form.Group className="mb-3" controlId="formBasicName">
        <Form.Control className='input' type="name" placeholder="Name *" />
      </Form.Group>
      <Form.Group className="mb-3" controlId="formBasicPassword">
        <Form.Control className='input' type="password" placeholder="Password *" />
      </Form.Group>
      <Form.Group className="mb-3" controlId="formBasicCheckPassword">
        <Form.Control className='input' type="check_password" placeholder="Repeat Password *" />
      </Form.Group>
      <Form.Group className="mb-3" controlId="formBasicLocation">
        <Form.Control className='input' type="location" placeholder="Location" />
      </Form.Group>
      <Form.Group className="mb-3" controlId="formBasicPosition">
        <Form.Control className='input' type="position" placeholder="Position" />
      </Form.Group>
      <div className="d-flex mb-4">
        <label className="area">Education Level</label>
        <Form.Select className="selectFrom" aria-label="Default select example">
          <option value="No-especificado">Choose</option>
          <option value="Primario">Primary</option>
          <option value="Secundario">Secondary</option>
          <option value="Terciario">Terciary</option>
          <option value="Graduado">Graduate</option>
          <option value="Postgrado">Postgraduate</option>
        </Form.Select>
      </div>
      <div className="d-flex mb-4">
        <label className="area">Job Category</label>
        <Form.Select className="selectFrom" aria-label="Default select example">
          {/* TODO: agregar for con las categories pasadas de la API*/}
          <option value="category">Category Name</option>
        </Form.Select>
      </div>
      <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
        <Form.Control placeholder='About Me' as="textarea" rows={3} />
      </Form.Group>
      </div>
      <p>(*) Fields are required.</p>
      {/* TODO: arreglar el metodo de link porque href es ilegal - funciona though*/}
      <Button href="/discoverJobs" variant="success" type="submit"><strong>Register</strong></Button>
    </Form>
    <div className="row">
    <div className="col mt-2 mb-2">
    <Button href="/login" variant="outline-secondary" type="submit"><strong>return</strong></Button>
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
    );
}

export default RegisterUser;