import * as Icon from "react-bootstrap-icons"
import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import defaultProfile from "../../images/defaultProfilePicture.png"
import CategoryDto from "../../utils/CategoryDto"
import { UserRole } from "../../utils/constants"
import { useSharedAuth } from "../../api/auth"
import { useRequestApi } from "../../api/apiRequest"
import { useTranslation } from "react-i18next"
import { useNavigate } from "react-router-dom"
import { useEffect, useMemo, useState } from "react"

function ProfileEnterpriseCard({ enterprise }: { enterprise: any }) {
  const navigate = useNavigate()
  const { t } = useTranslation()
  const { userInfo } = useSharedAuth()
  const { loading, apiRequest } = useRequestApi()

  const [loadingData, setLoadingData] = useState(true)
  const [enterpriseCategory, setEnterpriseCategory] = useState<CategoryDto | undefined>({} as CategoryDto)
  const [imageUrl, setImageUrl] = useState<string>("")

  const memorizedEnterprise = useMemo(() => enterprise, [enterprise])

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (loadingData) {
          const [categoryResponse, imageResponse] = await Promise.all([
            apiRequest({ url: memorizedEnterprise.links.category, method: "GET" }),
            apiRequest({ url: memorizedEnterprise.links.image, method: "GET" }),
          ])

          setEnterpriseCategory(categoryResponse.data)
          setImageUrl(imageResponse.status === 200 ? memorizedEnterprise.links.image : defaultProfile) //TODO: revisar si se puede hacer mejor
          setLoadingData(false)
        }
      } catch (error) {
        console.error("Error fetching data:", error)
      }
    }
    if (loadingData) fetchData()
  }, [apiRequest, loadingData, memorizedEnterprise])

  return (
    <Card className="profileCard rounded-3 mx-2" style={{ width: "14rem" }}>
      <Card.Img variant="top" src={imageUrl} />
      {userInfo?.role === UserRole.ENTERPRISE ? (
        <Button type="button" variant="success" onClick={() => navigate(`image`)}>
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
          {userInfo?.role === UserRole.ENTERPRISE ? (
            <Button
              className="float-end"
              type="button"
              variant="outline-success"
              onClick={() => navigate(`/editEnterprise/${enterprise.id}`)}
              style={{ paddingBottom: "10px" }}
            >
              <Icon.PencilSquare color="green" size={15} />
            </Button>
          ) : (
            <></>
          )}
        </div>
        {userInfo?.role === UserRole.ENTERPRISE ? <hr /> : <></>}
        <div>
          <div className="d-flex flex-column">
            <div className="d-flex justify-content-start my-2">
              <Icon.ListTask color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <div>{t("Job Category")}:</div>
              {enterprise.links.category !== "No-Especificado" ? (
                <div>
                  <Badge pill bg="success" className="mx-2">
                    {enterprise.links.category == null ? t("No-especificado") : enterpriseCategory?.name}
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
        </div>
      </Card.Body>
    </Card>
  )
}

export default ProfileEnterpriseCard
