import * as Icon from "react-bootstrap-icons"
import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import defaultProfile from "../../images/defaultProfilePicture.png"
import { useTranslation } from "react-i18next"
import { useNavigate, useParams } from "react-router-dom"
import { useEffect, useState, useMemo } from "react"
import { useRequestApi } from "../../api/apiRequest"
import { useSharedAuth } from "../../api/auth"
import CategoryDto from "../../utils/CategoryDto"

function ProfileUserCard({
  editable,
  contacted,
  user,
  inProfileView,
}: {
  editable: boolean
  contacted: boolean
  user: any
  inProfileView: boolean
}) {
  const navigate = useNavigate()
  const { t } = useTranslation()
  const { userInfo } = useSharedAuth()
  const { loading, apiRequest } = useRequestApi()
  const { id } = useParams()

  const [loadingData, setLoadingData] = useState(true)
  const [userCategory, setUserCategory] = useState<CategoryDto | undefined>({} as CategoryDto)
  const [skillsData, setSkillsData] = useState<any[]>([])
  const [imageUrl, setImageUrl] = useState<string>("")

  const memorizedUser = useMemo(() => user, [user])

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (loadingData) {
          const [skillsResponse, categoryResponse, imageResponse] = await Promise.all([
            apiRequest({ url: memorizedUser.skills, method: "GET" }),
            apiRequest({ url: memorizedUser.category, method: "GET" }),
            apiRequest({ url: memorizedUser.image, method: "GET" }),
          ])

          setSkillsData(skillsResponse.data)
          setUserCategory(categoryResponse.data)
          setImageUrl(imageResponse.status === 200 ? memorizedUser.image : defaultProfile) //TODO: revisar si se puede hacer mejor
          setLoadingData(false)
        }
      } catch (error) {
        console.error("Error fetching data:", error)
      }
    }
    if (loadingData) fetchData()
  }, [apiRequest, loadingData, memorizedUser])

  const userSkillsList = skillsData.map((skill, index) => {
    return (
      <Badge key={index} pill bg="success" className="mx-2">
        {skill.description}
      </Badge>
    )
  })

  return (
    <Card className="profileCard rounded-3 mx-2" style={{ width: "14rem", height: "10rem" }}>
      <Card.Img variant="top" src={imageUrl} />
      {userInfo?.role === "ENTERPRISE" ? (
        <></>
      ) : (
        <Button type="button" variant="success" onClick={() => navigate(`image`)}>
          <div className="d-flex align-items-center justify-content-center">
            <Icon.PlusSquare color="white" size={20} style={{ marginRight: "7px" }} />
            {t("Edit Profile Picture")}
          </div>
        </Button>
      )}
      <Card.Body style={{ alignContent: "left", alignItems: "left" }}>
        <div className="d-flex justify-content-around align-items-center">
          <h5>{user.name}</h5>
          {userInfo?.role === "USER" ? (
            <Button
              className="float-end"
              type="button"
              variant="outline-success"
              style={{ paddingBottom: "10px" }}
              onClick={() => navigate(`/editUser/${user.id}`)}
            >
              <Icon.PencilSquare color="green" size={15} />
            </Button>
          ) : !inProfileView ? (
            <></>
          ) : contacted ? (
            <Badge className="p-2" bg="secondary">
              {t("Contacted")}
            </Badge>
          ) : (
            <Button variant="outline-dark" onClick={() => navigate(`/enterprises/${userInfo?.id}/contacts/${id}`)}>
              {t("Contact")}
            </Button>
          )}
        </div>
        {editable ? <hr /> : <></>}
        <div>
          <div className="d-flex flex-column">
            <div className="d-flex justify-content-start my-2">
              <Icon.ListTask color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              {user.category !== "No-Especificado" ? (
                <div className="d-flex flex-row align-items-center">
                  {t("Category")}:
                  <Badge pill bg="success" className="mx-2" style={{ height: "fit-content" }}>
                    {user.category == null ? t("No-especificado") : userCategory?.name}
                  </Badge>
                </div>
              ) : (
                <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                  {t("Category")}: {t("No-especificado")}
                </p>
              )}
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.Briefcase color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Current Position")}:{" "}
                {user.currentPosition === "" || user.currentPosition == null
                  ? t("No-especificado")
                  : user.currentPosition}
              </p>
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.Book color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Education Level")}:{" "}
                {user.educationLevel === "" || user.educationLevel == null
                  ? t("No especificado")
                  : t(user.educationLevel)}
              </p>
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.GeoAltFill color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Location")}: {user.location === "" || user.location == null ? t("No especificado") : user.location}
              </p>
            </div>
            {inProfileView ? (
              <></>
            ) : (
              <div className="d-flex justify-content-start align-items-center my-2">
                {userSkillsList.length === 0 ? <div>{t("Skills Not Specified")}</div> : <div>{userSkillsList}</div>}
              </div>
            )}
          </div>
        </div>
      </Card.Body>
    </Card>
  )
}

ProfileUserCard.defaultProps = {
  editable: false,
  contacted: false,
  inProfileView: false,
}

export default ProfileUserCard
