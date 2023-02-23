import Button from 'react-bootstrap/Button';
import * as Icon from 'react-bootstrap-icons';
import Container from 'react-bootstrap/esm/Container';
import Form from 'react-bootstrap/Form';
import Card from 'react-bootstrap/Card';
import { useNavigate } from "react-router-dom";

function Login() {

  const navigate = useNavigate();

  const routeChange = () => {
      navigate("/header");
  }
  
  return (
    <div className="d-flex justify-content-between mt-2">
        <Container>
        <div className="row justify-content-center mt-0">
            <div className="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
            <Card className="custom-card px-0 pt-4 pb-0 mt-3 mb-3">
            <h2><strong>Welcome to ClonedIN!</strong></h2>
            <div className="row">
                        <div className="col-md-12 mx-0">
    <Form className='msform'>
        <div className="form-card">
      <Form.Group className="mb-3" controlId="formBasicEmail">
        <Form.Control className='input' type="email" placeholder="Email" />
      </Form.Group>
      <Form.Group className="mb-3" controlId="formBasicPassword">
        <Form.Control className='input' type="password" placeholder="Password" />
      </Form.Group>
      <Form.Group className="mb-3" controlId="formBasicCheckbox">
        <Form.Check type="checkbox" label="Remember me" />
      </Form.Group>
      </div>
      <Button className="action-button" type="submit" onClick={ routeChange }> Log In </Button>
      <p>No account yet? Choose your user type to register.</p>
    </Form>
    <div className="row">
      <div className="col">
        <Button className="action-button waves-effect"><Icon.Person size={40}/></Button>
        <p>Register as a User</p>
      </div>
      <div className="col">
        <Button className="action-button waves-effect"><Icon.Building size={40}/></Button>
        <p>Register as a Company</p>
      </div>
    </div>
    </div>
    </div>
    </Card>
    </div>
    </div>
    </Container>
    </div>
  );
}

export default Login;