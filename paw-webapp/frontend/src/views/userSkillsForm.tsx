import Button from "react-bootstrap/Button"
import Navigation from "../components/navbar"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import { useTranslation } from "react-i18next"
import { useNavigate, useParams } from "react-router-dom"
import { useSharedAuth } from "../api/auth"
import { useRequestApi } from "../api/apiRequest"
import { HttpStatusCode } from "axios"
import * as formik from "formik"
import * as yup from "yup"

function SkillsForm() {
  const navigate = useNavigate()
  const { t } = useTranslation()
  const { id } = useParams()
  const { userInfo } = useSharedAuth()
  const { loading, apiRequest } = useRequestApi()

  document.title = t("Skills Form Page Title")

  const { Formik } = formik

  const schema = yup.object().shape({
    skill: yup.string().required(t('Required') as string),
  })

  const handlePost = async (e: any) => {
    const skill = e.skill
    const response = await apiRequest({
      url: `/users/${id}/skills`,
      method: "POST",
      body: {
        skill,
      },
    })
    if (response.status === HttpStatusCode.Created) {
      navigate(`/users/${id}`)
    } else {
      //TODO: manejar error
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
                  <strong> {t("Skill add")} </strong>
                </h2>
                <p>{t("Fill all fields")}</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Formik
                      validationSchema={schema}
                      initialValues={{
                        skill: "",
                      }}
                      onSubmit={(values) => {
                        handlePost(values)
                      }}
                    >
                      {({ handleSubmit, handleChange, values, touched, errors }) => (
                        <Form className="msform" noValidate onSubmit={handleSubmit}>
                          <div className="form-card">
                            <h2 className="fs-title">{t("Skill")}</h2>
                            <Form.Group className="mb-3" controlId="formBasicSkill">
                              <Form.Control
                                name="skill"
                                className="input"
                                placeholder={t("Skill Ex").toString()}
                                value={values.skill}
                                onChange={(e) => {
                                  handleChange(e)
                                }}
                                isInvalid={!!errors.skill}
                              />
                              <Form.Control.Feedback type="invalid">{errors.skill}</Form.Control.Feedback>
                            </Form.Group>
                          </div>
                          <p>{t("Fields required")}</p>
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

export default SkillsForm
