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
import { monthNames } from "../utils/constants"
import { useTranslation } from "react-i18next"
import { useEffect, useState } from "react"
import { useParams, useNavigate } from "react-router-dom"
import { useSharedAuth } from "../api/auth"
import { HttpStatusCode } from "axios"
import { useGetUserData } from "../hooks/useGetUserData"
import { usePutUserData } from "../hooks/usePutUserData"
import { useDeleteUserData } from "../hooks/useDeleteUserData"

function ProfileUser() {
  const { getUserById, getUserExperiences, getUserEducations, getUserSkills } = useGetUserData()
  const { modifyUserVisibility } = usePutUserData()
  const { deleteUserSkill, deleteUserEducation, deleteUserExperience } = useDeleteUserData()

  const [user, setUser] = useState<UserDto | undefined>({} as UserDto)
  const [isUserLoading, setUserLoading] = useState(true)

  const [skillsData, setSkillsData] = useState<any[]>([])
  const [skillsLoading, setSkillsLoading] = useState(true)

  const [educationsData, setEducationsData] = useState<any[]>([])
  const [educationsLoading, setEducationsLoading] = useState(true)

  const [experiencesData, setExperiencesData] = useState<any[]>([])
  const [experiencesLoading, setExperiencesLoading] = useState(true)

  const { t } = useTranslation()
  const { id } = useParams()
  const { userInfo } = useSharedAuth()
  const navigate = useNavigate()

  useEffect(() => {
    const fetchUser = async () => {
      const response = await getUserById(id)

      if (response.status === HttpStatusCode.InternalServerError || response.status === HttpStatusCode.Forbidden) {
        navigate("/403")
      }
      setUser(response.data)
      setUserLoading(false)
    }

    const fetchSkills = async () => {
      const response = await getUserSkills(id)

      if (response.status === HttpStatusCode.Forbidden) {
        navigate("/403")
      }
      if (response.status === HttpStatusCode.NoContent) {
        setSkillsData([])
      } else {
        setSkillsData(response.data)
      }
      setSkillsLoading(false)
    }

    const fetchEducations = async () => {
      const response = await getUserEducations(id)

      if (response.status === HttpStatusCode.Forbidden) {
        navigate("/403")
      }
      if (response.status === HttpStatusCode.NoContent) {
        setEducationsData([])
      } else {
        setEducationsData(response.data)
      }
      setEducationsLoading(false)
    }

    const fetchExperiences = async () => {
      const response = await getUserExperiences(id)

      if (response.status === HttpStatusCode.Forbidden) {
        navigate("/403")
      }
      if (response.status === HttpStatusCode.NoContent) {
        setExperiencesData([])
      } else {
        setExperiencesData(response.data)
      }
      setExperiencesLoading(false)
    }

    if (isUserLoading) {
      fetchUser()
    }
    if (experiencesLoading) {
      fetchExperiences()
    }
    if (educationsLoading) {
      fetchEducations()
    }
    if (skillsLoading) {
      fetchSkills()
    }
  }, [
    isUserLoading,
    experiencesLoading,
    educationsLoading,
    skillsLoading,
    getUserById,
    getUserExperiences,
    getUserEducations,
    getUserSkills,
    id,
    navigate,
  ])

  const handleDelete = async (object: string, object_id: number) => {
    let response
    switch (object) {
      case "skill":
        response = await deleteUserSkill(id, object_id)
        if (response.status === HttpStatusCode.Ok) {
          setSkillsLoading(true)
        }
        break
      case "education":
        response = await deleteUserEducation(id, object_id)
        if (response.status === HttpStatusCode.Ok) {
          setEducationsLoading(true)
        }
        break
      case "experience":
        response = await deleteUserExperience(id, object_id)
        if (response.status === HttpStatusCode.Ok) {
          setExperiencesLoading(true)
        }
        break
    }
  }

  const handleVisibility = async () => {
    let visibility = "visible"
    if (user?.visibility === 1) visibility = "invisible"
    else visibility = "visible"

    const response = await modifyUserVisibility(id, visibility)

    if (response.status === HttpStatusCode.Ok) {
      setUserLoading(true)
    }
  }

  const userSkillsList = skillsData.map((skill) => {
    return (
      <div key={skill.id}>
        {userInfo?.role === "ENTERPRISE" ? (
          <Badge pill bg="success" text="light" className="mx-2 p-2">
            {skill.description}
          </Badge>
        ) : (
          <Badge pill bg="light" text="dark" className="mx-2">
            {skill.description}
            <Button
              type="button"
              variant="outline-dark"
              style={{ borderStyle: "none" }}
              onClick={() => handleDelete("skill", skill.id)}
            >
              <Icon.X />
            </Button>
          </Badge>
        )}
      </div>
    )
  })

  const userEducationsList = educationsData.map((education) => {
    return (
      <div key={education.id}>
        <div className="d-flex flex-row justify-content-between align-items-center">
          <h6>
            {education.institutionName} - {education.title}
          </h6>
          {userInfo?.role === "ENTERPRISE" ? (
            <></>
          ) : (
            <Button type="button" variant="outline-danger" onClick={() => handleDelete("education", education.id)}>
              <Icon.Trash />
            </Button>
          )}
        </div>
        <p style={{ fontSize: "10pt" }}>
          {t(monthNames[education.monthFrom])} {education.yearFrom}
          {" - "}
          {t(monthNames[education.monthTo])} {education.yearTo}
        </p>
        <p>{education.description}</p>
        <hr />
      </div>
    )
  })

  const userExperienceList = experiencesData.map((experience) => {
    return (
      <div key={experience.id}>
        <div className="d-flex flex-row justify-content-between align-items-center">
          <h6>
            {experience.enterpriseName} - {experience.position}
          </h6>
          {userInfo?.role === "ENTERPRISE" ? (
            <></>
          ) : (
            <Button type="button" variant="outline-danger" onClick={() => handleDelete("experience", experience.id)}>
              <Icon.Trash />
            </Button>
          )}
        </div>
        {experience.monthTo == null || experience.monthTo == 0 || experience.yearTo == null ? (
          <p style={{ fontSize: "10pt" }}>
            {t(monthNames[experience.monthFrom])} {experience.yearFrom}
            {" - "}
            {t("Present")}
          </p>
        ) : (
          <p style={{ fontSize: "10pt" }}>
            {t(monthNames[experience.monthFrom])} {experience.yearFrom}
            {" - "}
            {t(monthNames[experience.monthTo])} {experience.yearTo}
          </p>
        )}
        <p>{experience.description}</p>
        <hr />
      </div>
    )
  })

  document.title = user?.name + " | ClonedIn"

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <Container fluid style={{ background: "#F2F2F2", height: "100%", paddingBottom: "10px" }}>
        <Row className="row">
          <Col sm={3} className="col d-flex flex-column align-items-center">
            <br />
            {userInfo?.role === "ENTERPRISE" ? (
              <></>
            ) : (
              <Button variant="success" type="button" onClick={() => handleVisibility()}>
                {user?.visibility === 1 ? t("Hide My Profile") : t("Show My Profile")}
              </Button>
            )}
            {isUserLoading ? (
              <div className="my-5">
                <Loader />
              </div>
            ) : (
              <ProfileUserCard editable={true} user={user} inProfileView={true} />
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
                    {userInfo?.role === "ENTERPRISE" ? (
                      <></>
                    ) : (
                      <Button
                        type="button"
                        variant="success"
                        onClick={() => navigate(`experiences`)}
                        style={{ width: "200px" }}
                      >
                        <Icon.PlusSquare color="white" style={{ marginRight: "7px" }} />
                        {t("Add Experience")}
                      </Button>
                    )}
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
                    {userInfo?.role === "ENTERPRISE" ? (
                      <></>
                    ) : (
                      <Button
                        type="button"
                        variant="success"
                        onClick={() => navigate(`educations`)}
                        style={{ width: "200px" }}
                      >
                        <Icon.PlusSquare color="white" style={{ marginRight: "7px" }} />
                        {t("Add Education")}
                      </Button>
                    )}
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
                    {userInfo?.role === "ENTERPRISE" ? (
                      <></>
                    ) : (
                      <Button
                        type="button"
                        variant="success"
                        onClick={() => navigate(`skills`)}
                        style={{ width: "200px" }}
                      >
                        <Icon.PlusSquare color="white" style={{ marginRight: "7px" }} />
                        {t("Add Skill")}
                      </Button>
                    )}
                  </div>
                </Card.Title>
                <hr />
                {userSkillsList.length > 0 ? (
                  <div className="d-flex">{userSkillsList}</div>
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
