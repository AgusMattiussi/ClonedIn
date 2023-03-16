import Button from 'react-bootstrap/Button';
import * as Icon from 'react-bootstrap-icons';
import Container from 'react-bootstrap/esm/Container';
import Row from 'react-bootstrap/esm/Row';
import Col from 'react-bootstrap/esm/Col';
import Card from 'react-bootstrap/Card';
import Navigation from '../components/navbar'
import Form from 'react-bootstrap/Form';
import Dropdown from 'react-bootstrap/Dropdown';
import defaultProfile from '../images/defaultProfilePicture.png'


function ProfileUser() {

  return (
    <div>
      <Navigation/>
      <Container fluid>
        <Row className="row">
            <Col sm={3} className="col">
                <br/>
                <Button variant="success" type="button">Hide Profile</Button>
                <br/>
                <Card className="profileCard" style={{ width: '12rem' } }>
                    <Card.Img variant="top" src={defaultProfile} />
                    <Button type="button" variant="success">
                    <Icon.PlusSquare color="white" size={20} style={{ marginRight: '5px' }} />
                    Edit Profile Picture
                    </Button>
                    <Card.Body style={{ alignContent: 'left', alignItems: 'left'}}>
                        <Card.Title>
                            Username 
                            <Button type="button" variant="outline-success" style={{ marginLeft: '15px' }}>
                            <Icon.PencilSquare color="green" size={15} />
                            </Button>   
                        </Card.Title>
                        <Card.Text> 
                        <Icon.ListTask color="black" size={15} style={{ marginRight: '15px' }}/>
                        Job Category
                        <br/>
                        <Icon.Briefcase color="black" size={15} style={{ marginRight: '15px' }}/>
                        Current Position
                        <br/>
                        <Icon.Book color="black" size={15} style={{ marginRight: '15px' }}/>
                        Educational Level
                        <br/>
                        <Icon.GeoAltFill color="black" size={15} style={{ marginRight: '15px' }}/>
                        Location
                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
            <Col sm={8} className="col">
                <br/>
                <Card>
                    <Card.Header>
                        <strong>About Me</strong>
                    </Card.Header>
                    <Card.Body>
                        Lorem ipsum
                    </Card.Body>
                </Card>
                <br/>
                <Card>
                    <Card.Header>
                        <strong>Experience</strong>
                        <Button type="button" variant="success" style={{ marginLeft: '15px' }}>
                            <Icon.PlusSquare color="white" size={15} style={{ marginRight: '5px' }} />
                            Add Experience
                        </Button>   
                    </Card.Header>
                    <Card.Body>
                        Lorem ipsum
                    </Card.Body>
                </Card>
                <br/>
                <Card>
                    <Card.Header>
                        <strong>Education</strong>
                        <Button type="button" variant="success" style={{ marginLeft: '15px' }}>
                            <Icon.PlusSquare color="white" size={15} style={{ marginRight: '5px' }} />
                            Add Education
                        </Button>   
                    </Card.Header>
                    <Card.Body>
                        Lorem ipsum
                    </Card.Body>
                </Card>
                <br/>
                <Card>
                    <Card.Header>
                        <strong>Skills</strong>
                        <Button type="button" variant="success" style={{ marginLeft: '15px' }}>
                            <Icon.PlusSquare color="white" size={15} style={{ marginRight: '5px' }} />
                            Add Skill
                        </Button>   
                    </Card.Header>
                    <Card.Body>
                        Lorem ipsum
                    </Card.Body>
                </Card>
            </Col>
        </Row>
      </Container>
    </div>
  );
}

export default ProfileUser;