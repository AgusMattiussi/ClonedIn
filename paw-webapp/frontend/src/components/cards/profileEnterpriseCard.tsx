import * as Icon from "react-bootstrap-icons"
import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import defaultProfile from "../../images/defaultProfilePicture.png"
import CategoryDto from "../../utils/CategoryDto"
import { useSharedAuth } from "../../api/auth"
import { useRequestApi } from "../../api/apiRequest"
import { useTranslation } from "react-i18next"
import { useEffect, useState } from "react"

function ProfileEnterpriseCard({ editable, enterprise }: { editable: boolean; enterprise: any }) {
  const { t } = useTranslation()
  const { userInfo } = useSharedAuth()
  const { loading, apiRequest } = useRequestApi()
  const [enterpriseCategory, setEnterpriseCategory] = useState<CategoryDto | undefined>({} as CategoryDto)

  useEffect(() => {
    const fetchCategory = async () => {
      const response = await apiRequest({
        url: enterprise.category,
        method: "GET",
      })
      setEnterpriseCategory(response.data)
    }

    if (enterpriseCategory === null) {
      fetchCategory()
    }
  }, [apiRequest])

  return (
    <Card className="profileCard rounded-3 mx-2" style={{ width: "14rem" }}>
      <Card.Img variant="top" src={defaultProfile} />
      {userInfo?.role === "ENTERPRISE" ? (
        <Button type="button" variant="success" href="/imageProfile">
          <div className="d-flex align-items-center justify-content-center">
            <Icon.PlusSquare color="white" size={20} style={{ marginRight: "7px" }} />
            {t("Edit Profile Picture")}
          </div>
        </Button>
      ) : (
        <></>
      )}
      <Card.Body style={{ alignContent: "left", alignItems: "left" }}>
        <div className="d-flex justify-content-around align-items-center">
          <h5>{enterprise.name}</h5>
          {userInfo?.role === "ENTERPRISE" ? (
            <Button
              className="float-end"
              type="button"
              variant="outline-success"
              href={`/editEnterprise/${enterprise.id}`}
              style={{ paddingBottom: "10px" }}
            >
              <Icon.PencilSquare color="green" size={15} />
            </Button>
          ) : (
            <></>
          )}
        </div>
        {userInfo?.role === "ENTERPRISE" ? <hr /> : <></>}
        <Card.Text>
          <div className="d-flex flex-column">
            <div className="d-flex justify-content-start my-2">
              <Icon.ListTask color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              {enterprise.category !== "No-Especificado" ? (
                <div>
                  {t("Job Category")}:
                  <Badge pill bg="success" className="mx-2">
                    {enterprise.category == null ? t("No-especificado") : enterpriseCategory?.name}
                  </Badge>
                </div>
              ) : (
                <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                  {t("Job Category")}: {t("No-especificado")}
                </p>
              )}
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.PeopleFill color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Quantity of employees")}: {enterprise.workers}
              </p>
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.CalendarEvent color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Funding Year")}: {enterprise.year}
              </p>
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.Globe color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Website")}: {enterprise.link}
              </p>
            </div>
            <div className="d-flex flex-column align-items-start my-2">
              <p className="fw-bold" style={{ marginBottom: "2px" }}>
                {t("About Us")}
              </p>
              <p> {enterprise.description}</p>
            </div>
          </div>
        </Card.Text>
      </Card.Body>
    </Card>
  )
}

ProfileEnterpriseCard.defaultProps = {
  editable: false,
}

export default ProfileEnterpriseCard
