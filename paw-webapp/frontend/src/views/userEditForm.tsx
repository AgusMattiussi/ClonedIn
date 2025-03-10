import Button from "react-bootstrap/Button"
import Navigation from "../components/navbar"
import Container from "react-bootstrap/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import UserDto from "../utils/UserDto"
import { educationLevels } from "../utils/constants"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate, useParams } from "react-router-dom"
import { useSharedAuth } from "../api/auth"
import { usePostUserInfo } from "../hooks/usePostUserInfo"
import { useGetCategories } from "../hooks/useGetCategories"
import { useGetUserById } from "../hooks/useGetUserById"
import { HttpStatusCode } from "axios"
import * as formik from "formik"
import * as yup from "yup"

function EditUserForm() {
  const navigate = useNavigate()

  const { t } = useTranslation()
  const { id } = useParams()
  const { userInfo } = useSharedAuth()
  const { getUserById } = useGetUserById()
  const { getCategories } = useGetCategories()
  const { modifyUserInfo } = usePostUserInfo()

  const [categoryList, setCategoryList] = useState([])

  document.title = t("Edit Page Title")

  const { Formik } = formik

  const schema = yup.object().shape({
    name: yup.string().max(100, t("Line Max Length") as string),
    location: yup.string().max(50, t("Single Line Max Length") as string),
    position: yup.string().max(50, t("Single Line Max Length") as string),
    aboutMe: yup.string().max(600, t("Long Line Max Length") as string),
  })
  const [user, setUser] = useState<UserDto | undefined>({} as UserDto)
  const [category, setCategory] = useState(user?.categoryInfo?.name)
  const [level, setLevel] = useState(user?.educationLevel)

  useEffect(() => {
    const fetchUser = async () => {
      const response = await getUserById(id)
      if (response.status === HttpStatusCode.Ok) {
        setUser(response.data)
      } else {
        console.error("Error getting user information", response)
      }
    }

    const fetchCategories = async () => {
      const response = await getCategories()
      setCategoryList(response.data)
    }

    if (categoryList.length === 0) {
      fetchCategories()
      fetchUser()
    }
  }, [id, categoryList.length, getUserById, getCategories, navigate])

  const handlePost = async (e: any) => {
    const name = e.name
    const location = e.location
    const position = e.position
    const aboutMe = e.aboutMe
    const response = await modifyUserInfo(id, name, location, position, aboutMe, category, level)
    if (response.status === HttpStatusCode.Ok) {
      navigate(`/users/${id}`)
    } else {
      console.error("Error updating user", response)
    }
  }

  const handleEducationLevelSelect = (e: any) => {
    if (e.target.value === "No-especificado" || educationLevels.includes(e.target.value)) {
      setLevel(e.target.value)
    } else {
      alert("ERROR")
    }
  }

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <div className="d-flex justify-content-between mt-2">
        <Container>
          <div className="row justify-content-center mt-0">
            <div className="col-11 col-sm-9 col-md-7 col-lg-6 p-0 mt-3 mb-2">
              <Card className="custom-card px-0 pt-4 pb-0 mt-3 mb-3">
                <h2 className="text-center p-0 mt-3 mb-2">
                  <strong>{t("Edit")}</strong>
                </h2>
                <p>{t("Fill some fields")}</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Formik
                      validationSchema={schema}
                      initialValues={{
                        name: "",
                        location: "",
                        position: "",
                        aboutMe: "",
                      }}
                      onSubmit={(values) => {
                        handlePost(values)
                      }}
                    >
                      {({ handleSubmit, handleChange, values, touched, errors }) => (
                        <Form className="msform" noValidate onSubmit={handleSubmit}>
                          <div className="form-card">
                            <h2 className="fs-title">{t("Basic Information")}</h2>
                            <Form.Group className="mb-3 mt-3" controlId="formBasicName">
                              <Form.Control
                                name="name"
                                className="input"
                                placeholder={user?.name}
                                value={values.name}
                                onChange={handleChange}
                                isInvalid={!!errors.name}
                              />
                              <Form.Control.Feedback type="invalid">{errors.name}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicLocation">
                              <Form.Control
                                name="location"
                                className="input"
                                placeholder={
                                  user?.location === "" ? t("Location").toString() : user?.location || undefined
                                }
                                value={values.location}
                                onChange={handleChange}
                                isInvalid={!!errors.location}
                              />
                              <Form.Control.Feedback type="invalid">{errors.location}</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicPosition">
                              <Form.Control
                                name="position"
                                className="input"
                                placeholder={
                                  user?.currentPosition === ""
                                    ? t("Current Position").toString()
                                    : user?.currentPosition || undefined
                                }
                                value={values.position}
                                onChange={handleChange}
                                isInvalid={!!errors.position}
                              />
                              <Form.Control.Feedback type="invalid">{errors.position}</Form.Control.Feedback>
                            </Form.Group>
                            <div className="d-flex mb-4">
                              <label
                                className="area"
                                style={{
                                  width: "100px",
                                }}
                              >
                                {t("Education Level")}
                              </label>
                              <Form.Select
                                className="selectFrom"
                                aria-label="Default select example"
                                value={level}
                                onChange={handleEducationLevelSelect}
                              >
                                <option value="No-especificado">{t("No-especificado")}</option>
                                <option value="Primario">{t("Primario")}</option>
                                <option value="Secundario">{t("Secundario")}</option>
                                <option value="Terciario">{t("Terciario")}</option>
                                <option value="Graduado">{t("Graduado")}</option>
                                <option value="Postgrado">{t("Postgrado")}</option>
                              </Form.Select>
                            </div>
                            <div className="d-flex mb-4">
                              <label
                                className="area"
                                style={{
                                  width: "100px",
                                }}
                              >
                                {t("Job Category")}
                              </label>
                              <Form.Select
                                className="selectFrom"
                                aria-label="Default select example"
                                value={category}
                                onChange={(e) => setCategory(e.target.value)}
                              >
                                <option key="1" value="No-Especificado">
                                  {t("No-especificado")}
                                </option>
                                {categoryList.map((categoryListItem: any) => (
                                  <option key={categoryListItem.id} value={categoryListItem.name}>
                                    {t(categoryListItem.name)}
                                  </option>
                                ))}
                              </Form.Select>
                            </div>
                            <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                              <Form.Control
                                name="aboutMe"
                                placeholder={
                                  user?.description === "" ? t("About Me").toString() : user?.description || undefined
                                }
                                as="textarea"
                                rows={3}
                                value={values.aboutMe}
                                onChange={handleChange}
                                isInvalid={!!errors.aboutMe}
                              />
                              <Form.Control.Feedback type="invalid">{errors.aboutMe}</Form.Control.Feedback>
                            </Form.Group>
                          </div>
                          <p> {t("Fields required")}</p>
                          <Button variant="success" type="submit">
                            <strong>{t("Save")}</strong>
                          </Button>
                        </Form>
                      )}
                    </Formik>
                    <div className="row">
                      <div className="col mt-2 mb-2">
                        <Button onClick={() => navigate(`/users/${id}`)} variant="outline-secondary">
                          <strong>{t("Return")}</strong>
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

export default EditUserForm
