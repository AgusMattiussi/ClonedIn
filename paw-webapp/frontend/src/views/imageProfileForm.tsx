import Button from "react-bootstrap/Button"
import Navigation from "../components/navbar"
import Container from "react-bootstrap/esm/Container"
import Form from "react-bootstrap/Form"
import Card from "react-bootstrap/Card"
import React, { useState } from "react"
import { useTranslation } from "react-i18next"
import { useNavigate, useParams } from "react-router-dom"
import { useSharedAuth } from "../api/auth"
import { usePutImage } from "../hooks/usePutImage"
import { HttpStatusCode } from "axios"

function ImageProfileForm() {
  const navigate = useNavigate()
  const { t } = useTranslation()
  const { id } = useParams()
  const { userInfo } = useSharedAuth()
  const { putImage } = usePutImage()
  const [imageFile, setImageFile] = useState(null)

  document.title = t("Image Form Page Title")

  const handleFileChange = (e: any) => {
    setImageFile(e.target.files[0])
  }

  const handlePut = async (e: any) => {
    if (!imageFile) {
      return
    }

    const formData = new FormData()
    formData.append("image", imageFile)

    let requestUrl = ""
    if (userInfo?.role === "ENTERPRISE") {
      requestUrl = `/enterprises/${id}/image`
    } else {
      requestUrl = `/users/${id}/image`
    }

    const response = await putImage(requestUrl, formData)

    if (response.status === HttpStatusCode.Ok) {
      navigate(-1)
    } else {
      console.error("Error uploading image")
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
                  <strong>{t("Image")}</strong>
                </h2>
                <div className="row">
                  <div className="col-md-12 mx-0">
                    <Form className="msform">
                      <div className="form-card">
                        <Form.Group controlId="formFile" className="mb-3">
                          <Form.Control type="file" onChange={handleFileChange} />
                        </Form.Group>
                      </div>
                      <Button variant="success" onClick={handlePut}>
                        <strong>{t("Upload")}</strong>
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

export default ImageProfileForm
