import Button from 'react-bootstrap/Button';
import Header from '../components/header';
import Container from 'react-bootstrap/esm/Container';
import Form from 'react-bootstrap/Form';
import Card from 'react-bootstrap/Card';
import * as React from 'react';
import {useState, useEffect} from 'react';

function RegisterUser() {

  const [categoryList, setCategoryList] = useState([])
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [repeatPassword, setRepeatPassword] = useState('');
  const [name, setName] = useState('');
  const [city, setCity] = useState('');
  const [category, setCategory] = useState('');
  const [position, setPosition] = useState('');
  const [description, setDescription] = useState('');
  const [studiesLevel, setStudiesLevel] = useState('');
  const [error, setError] = useState(null);

  /* Cargar lista de rubros */
  useEffect(() => {
    fetch("http://localhost:8080/webapp_war/categories")
      .then((response) => response.json())
      .then((response) => {
        setCategoryList(response);
        setError(null);
      })
      .catch(setError);
  }, []);

  /* Registra un usuario con los datos obtenidos del formulario */
  const register = async (email: string, password: string, repeatPassword: string, name: string, city: string,
     category: string, position: string, description: string, studiesLevel: string) => {
    await fetch('http://localhost:8080/webapp_war/users', {
        method: 'POST',
        body: JSON.stringify({
          email: email,
          password: password,
          repeatPassword: repeatPassword,
          name: name,
          city: city,
          position: position,
          aboutMe: description,
          category: category,
          level: studiesLevel
        }),
        headers: {
          'Content-type': 'application/json; charset=UTF-8',
        },
      })
      .then((response) => response.json())
      .then((data) => {
        setEmail('');
        setPassword('');
        setRepeatPassword('');
        setName('');
        setCity('');
        setCategory('');
        setPosition('');
        setDescription('');
        setStudiesLevel('');
      })
      .catch((err) => {
        console.log(err.message);
      });
  };

  const handleSubmit = (e: any) => {
    e.preventDefault();
    /* console.log('Registrando...');
    console.log('JSON ENVIADO: ' + JSON.stringify({
      email: email,
        password: password,
        repeatPassword: repeatPassword,
        name: name, 
        city: city,
        position: position,        
        aboutMe: description,
        category: category, 
        level: studiesLevel
    })); */
    register(email, password, repeatPassword, name, city, category, position, description, studiesLevel);
  };

  /* TODO: En caso de que haya ERRORS, devolver pantalla adecuada */
  return (
    <div>
      <Header />
      <div className="d-flex justify-content-between mt-2">
        <Container>
          <div className="row justify-content-center mt-0">
            <div className="col-11 col-sm-9 col-md-7 col-lg-6 p-0 mt-3 mb-2">
              <Card className="custom-card px-0 pt-4 pb-0 mt-3 mb-3">
                <h2 className="text-center p-0 mt-3 mb-2"><strong>Register</strong></h2>
                <p>Make sure to fill all fields before advancing.</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Form className='msform' onSubmit={handleSubmit}>
                      <div className="form-card">
                        <h2 className="fs-title">Basic Information</h2>
                        <Form.Group className="mb-3" controlId="formBasicEmail">
                          <Form.Control className='input' type="email" placeholder="Email *" value={email}
                            onChange={(e)=> setEmail(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicName">
                          <Form.Control className='input' placeholder="Name *" value={name} onChange={(e)=>
                            setName(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicPassword">
                          <Form.Control className='input' type="password" placeholder="Password *" value={password}
                            onChange={(e)=> setPassword(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicCheckPassword">
                          <Form.Control className='input' type="password" placeholder="Repeat Password *"
                            value={repeatPassword} onChange={(e)=> setRepeatPassword(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicLocation">
                          <Form.Control className='input' placeholder="Location" value={city} onChange={(e)=>
                            setCity(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicPosition">
                          <Form.Control className='input' placeholder="Position" value={position} onChange={(e)=>
                            setPosition(e.target.value)} />
                        </Form.Group>
                        <div className="d-flex mb-4">
                          <label className="area">Education Level</label>
                          <Form.Select className="selectFrom" value={studiesLevel}
                            onChange={(e)=> setStudiesLevel(e.target.value)}>
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
                          <Form.Select className="selectFrom" value={category}
                            onChange={(e)=> setCategory(e.target.value)}>
                            {categoryList.map((categoryListItem: any) => (
                            <option key={categoryListItem.id} value={categoryListItem.name}>
                              {categoryListItem.name}
                            </option>
                            ))}
                          </Form.Select>
                        </div>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                          <Form.Control placeholder='About Me' as="textarea" rows={3} value={description}
                            onChange={(e)=> setDescription(e.target.value)} />
                        </Form.Group>
                      </div>
                      <p>(*) Fields are required.</p>
                      {/* TODO: arreglar el metodo de link porque href es ilegal - funciona though*/}
                      <Button variant="success" type="submit"><strong>Register</strong></Button>
                    </Form>
                    <div className="row">
                      <div className="col mt-2 mb-2">
                        <Button href="/login" variant="outline-secondary"><strong>return</strong></Button>
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