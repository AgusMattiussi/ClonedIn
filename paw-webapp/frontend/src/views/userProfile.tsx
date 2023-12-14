import Button from "react-bootstrap/Button"
import * as Icon from "react-bootstrap-icons"
import Container from "react-bootstrap/esm/Container"
import Row from "react-bootstrap/esm/Row"
import Col from "react-bootstrap/esm/Col"
import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import ProfileUserCard from "../components/cards/profileUserCard"
import Navigation from "../components/navbar"
import Loader from "../components/loader"
import UserDto from "../utils/UserDto"
import GetUserData from "../api/userApi"
import { monthNames, BASE_URL } from "../utils/constants"
import { useTranslation } from "react-i18next"
import { useEffect, useState } from "react"
import { useParams } from "react-router-dom"
import { useRequestApi } from "../api/apiRequest"

function ProfileUser() {
  //TODO: Fix this to show specific user info
  const { loading, apiRequest } = useRequestApi()
  const [isUserLoading, setUserLoading] = useState(true)
  const [user, setUser] = useState<UserDto | undefined>({} as UserDto)
  const [error, setError] = useState(null)

  const { t } = useTranslation()
  const { id } = useParams()

  const USER_API_URL = BASE_URL + `/users/${id}/`

  useEffect(() => {
    const fetchUser = async () => {
      const response = await apiRequest({
        url: `/users/${id}/`,
        method: "GET",
      })
      setUser(response.data)
      setUserLoading(false)
      setError(null)
    }

    if (user === undefined) {
      fetchUser()
    }
  }, [apiRequest, id])

  const userSkillsList = GetUserData(USER_API_URL + "skills").map((skill) => {
    return (
      <Badge pill bg="light" text="dark" className="mx-2">
        {skill.description}
        <Button type="button" variant="outline-dark" style={{ borderStyle: "none" }}>
          <Icon.X />
        </Button>
      </Badge>
    )
  })

  const userEducationsList = GetUserData(USER_API_URL + "educations").map((education) => {
    return (
      <>
        <div className="d-flex flex-row justify-content-between align-items-center">
          <h6>
            {education.institutionName} - {education.title}
          </h6>
          <Button type="button" variant="outline-danger">
            <Icon.Trash />
          </Button>
        </div>
        <p style={{ fontSize: "10pt" }}>
          {/* TODO: agregar condicionales si no especifico fecha */}
          {t(monthNames[education.monthFrom])} {education.yearFrom}
          {" - "}
          {t(monthNames[education.monthTo])} {education.yearTo}
        </p>
        <p>{education.description}</p>
        <hr />
      </>
    )
  })

  const userExperienceList = GetUserData(USER_API_URL + "experiences").map((experience) => {
    return (
      <>
        <div className="d-flex flex-row justify-content-between align-items-center">
          <h6>
            {experience.enterpriseName} - {experience.position}
          </h6>
          <Button type="button" variant="outline-danger">
            <Icon.Trash />
          </Button>
        </div>
        <p style={{ fontSize: "10pt" }}>
          {/* TODO: agregar condicionales si no especifico fecha */}
          {t(monthNames[experience.monthFrom])} {experience.yearFrom}
          {" - "}
          {t(monthNames[experience.monthTo])} {experience.yearTo}
        </p>
        <p>{experience.description}</p>
        <hr />
      </>
    )
  })

  document.title = user?.name + " | ClonedIn"

  return (
    <div>
      <Navigation />
      <Container fluid style={{ background: "#F2F2F2", height: "100%", paddingBottom: "10px" }}>
        <Row className="row">
          <Col sm={3} className="col d-flex flex-column align-items-center">
            <br />
            <Button variant="success" type="button">
              {t("Hide My Profile")}
            </Button>
            {isUserLoading ? (
              <div className="my-5">
                <Loader />
              </div>
            ) : (
              <ProfileUserCard editable={true} user={user} />
            )}
          </Col>
          <Col sm={8} className="col">
            <br />
            <Card style={{ textAlign: "left" }}>
              <Card.Body>
                <Card.Title>
                  <strong>{t("About Me")}</strong>
                </Card.Title>
                <hr />
                {isUserLoading ? (
                  <div className="my-1">
                    <Loader />
                  </div>
                ) : user?.description === "" ? (
                  <div style={{ fontWeight: "bold" }}>{t("No especificado")}</div>
                ) : (
                  <div>{user?.description}</div>
                )}
              </Card.Body>
            </Card>
            <br />
            <Card style={{ textAlign: "left" }}>
              <Card.Body>
                <Card.Title>
                  <div className="d-flex flex-row justify-content-between align-items-center">
                    <strong>{t("Experience")}</strong>
                    <Button type="button" variant="success" href="/experiences" style={{ width: "200px" }}>
                      <Icon.PlusSquare color="white" style={{ marginRight: "7px" }} />
                      {t("Add Experience")}
                    </Button>
                  </div>
                </Card.Title>
                <hr />
                {userExperienceList.length > 0 ? (
                  <div>{userExperienceList}</div>
                ) : (
                  <div style={{ fontWeight: "bold" }}>{t("Experience Not Specified")}</div>
                )}
              </Card.Body>
            </Card>
            <br />
            <Card style={{ textAlign: "left" }}>
              <Card.Body>
                <Card.Title>
                  <div className="d-flex flex-row justify-content-between align-items-center">
                    <strong>{t("Education Level")}</strong>
                    <Button type="button" variant="success" href="/educations" style={{ width: "200px" }}>
                      <Icon.PlusSquare color="white" style={{ marginRight: "7px" }} />
                      {t("Add Education")}
                    </Button>
                  </div>
                </Card.Title>
                <hr />
                {userEducationsList.length > 0 ? (
                  <div>{userEducationsList}</div>
                ) : (
                  <div style={{ fontWeight: "bold" }}>{t("Education Not Specified")}</div>
                )}
              </Card.Body>
            </Card>
            <br />
            <Card style={{ textAlign: "left" }}>
              <Card.Body>
                <Card.Title>
                  <div className="d-flex flex-row justify-content-between align-items-center">
                    <strong>{t("Skills")}</strong>
                    <Button type="button" variant="success" href="/skills" style={{ width: "200px" }}>
                      <Icon.PlusSquare color="white" style={{ marginRight: "7px" }} />
                      {t("Add Skill")}
                    </Button>
                  </div>
                </Card.Title>
                <hr />
                {/* View As Enterprise
                <Badge pill bg="success" className="mx-2 p-2">
                  skill1
                </Badge> */}
                {userSkillsList.length > 0 ? (
                  <div>{userSkillsList}</div>
                ) : (
                  <div style={{ fontWeight: "bold" }}>{t("Skills Not Specified")}</div>
                )}
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  )
}

export default ProfileUser
