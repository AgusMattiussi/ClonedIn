import Button from "react-bootstrap/Button"
import Navigation from "../components/navbar"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate, useParams } from "react-router-dom"
import { useSharedAuth } from "../api/auth"
import { useRequestApi } from "../api/apiRequest"
import { HttpStatusCode } from "axios"
import Loader from "../components/loader"
import * as formik from "formik"
import * as yup from "yup"
import JobOfferDto from "../utils/JobOfferDto"

function ContactForm() {
  const navigate = useNavigate()

  const { t } = useTranslation()
  const { userInfo } = useSharedAuth()
  const { userId } = useParams()

  const { loading, apiRequest } = useRequestApi()
  const [userName, setUserName] = useState("")
  const [jobOffersList, setJobOffersList] = useState<any[]>([])
  const [jobOffersLoading, setJobOffersLoading] = useState(true)
  const [jobOfferId, setJobOfferId] = useState("No-especificado")
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchJobOffers = async () => {
      let queryParams: Record<string, string> = {}
      queryParams.onlyActive = "true"
      const response = await apiRequest({
        url: `enterprises/${userInfo?.id}/jobOffers`,
        method: "GET",
        queryParams: queryParams,
      })
      if (response.status === HttpStatusCode.NoContent) {
        setJobOffersList([])
      } else {
        setJobOffersList(response.data)
      }
      setJobOffersLoading(false)
    }

    const fetchUserName = async () => {
      const response = await apiRequest({
        url: `/users/${userId}`,
        method: "GET",
      })
      setUserName(response.data.name)
    }

    if (jobOffersLoading) {
      fetchJobOffers()
    }
    if (userName.length === 0) {
      fetchUserName()
    }
  }, [apiRequest, jobOffersLoading, userInfo?.id, userName.length, userId])

  const { Formik } = formik

  const schema = yup.object().shape({
    message: yup.string().max(200, t('Multi Line Max Length') as string),
  })

  const handlePost = async (e: any) => {
    const message = e.message
    const response = await apiRequest({
      url: `/enterprises/${userInfo?.id}/contacts`,
      method: "POST",
      body: {
        message,
        jobOfferId,
        userId,
      },
    })
    if (response.status === HttpStatusCode.Created) {
      navigate(`/users/${userId}`)
    } else {
      setError(t("Must Choose") as string)
    }
  }

  document.title = t("Contact Form Page Title")

  return (
    <div>
      <Navigation role={userInfo?.role} />
      <div className="d-flex justify-content-between mt-2">
        <Container>
          <div className="row justify-content-center mt-0">
            <div className="col-11 col-sm-9 col-md-7 col-lg-6 p-0 mt-3 mb-2">
              <Card className="custom-card px-0 pt-4 pb-0 mt-3 mb-3">
                <h2 className="text-center p-0 mt-3 mb-2">
                  <strong>
                    {" "}
                    {t("Send message")} {userName}
                  </strong>
                </h2>
                <p>{t("Fill all fields")}</p>
                <div className="row">
                  <div className="col-md-12 mx-0">
                  <Formik
                      validationSchema={schema}
                      initialValues={{
                        message: "",
                      }}
                      onSubmit={(values) => {
                        handlePost(values)
                      }}
                    >
                      {({ handleSubmit, handleChange, values, touched, errors }) => (
                      <Form className="msform" onSubmit={handleSubmit}>
                        <div className="form-card">
                          <h2 className="fs-title"> {t("Message")} </h2>
                          <Form.Group className="mb-3 mt-3" controlId="formBasicMessage">
                            <Form.Control
                              name="message"
                              className="input"
                              placeholder={t("Candidate").toString()}
                              value={values.message}
                              onChange={handleChange}
                              isInvalid={!!errors.message}
                            />
                            <Form.Control.Feedback type="invalid">{errors.message}</Form.Control.Feedback>
                          </Form.Group>
                          <div className="d-flex mb-4">
                            <label className="area">{t("Job Offers ")}</label>
                            {jobOffersLoading ? (
                              <Loader />
                            ) : (
                              <Form.Select
                                className="selectFrom"
                                aria-label="Default select example"
                                value={jobOfferId}
                                onChange={(e) => setJobOfferId(e.target.value)}
                              >
                                <option key="0" value="No-especificado"> {t("No-especificado")} </option>
                                {jobOffersList.map((jobOffer: JobOfferDto) => (
                                  <option key={jobOffer.id} value={jobOffer.id}>
                                    {jobOffer.position}
                                  </option>
                                ))}
                              </Form.Select>
                            )}
                          </div>
                          {error && <div className="error" style={{color: "red"}}>{error}</div>}
                        </div>
                        <p>{t("Fields required")}</p>
                        <Button variant="success" type="submit">
                          <strong>{t("Contact")}</strong>
                        </Button>
                      </Form>
                    )}
                    </Formik>
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

export default ContactForm
