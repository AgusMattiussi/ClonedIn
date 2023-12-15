import Button from "react-bootstrap/Button"
import Navigation from "../components/navbar"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router-dom"
import { useSharedAuth } from "../api/auth"

function SkillsForm() {
  const { t } = useTranslation()
  const navigate = useNavigate()
  const { userInfo } = useSharedAuth()

  document.title = t("Skills Form Page Title")

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
                    <Form className="msform">
                      <div className="form-card">
                        <h2 className="fs-title">{t("Skill")}</h2>
                        <Form.Group className="mb-3 mt-3" controlId="formBasicSkill">
                          <Form.Control className="input" placeholder={t("Skill Ex").toString()} />
                        </Form.Group>
                      </div>
                      <p>{t("Fields required")}</p>
                      <Button onClick={() => navigate(-1)} variant="success" type="submit">
                        <strong>{t("Save")}</strong>
                      </Button>
                    </Form>
                    <div className="row">
                      <div className="col mt-2 mb-2">
                        <Button onClick={() => navigate(-1)} variant="outline-secondary">
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
