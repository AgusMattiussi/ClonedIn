import * as Icon from "react-bootstrap-icons"
import Card from "react-bootstrap/Card"
import Badge from "react-bootstrap/Badge"
import Button from "react-bootstrap/Button"
import defaultProfile from "../../images/defaultProfilePicture.png"
import CategoryDto from "../../utils/CategoryDto"
import { UserRole } from "../../utils/constants"
import { useTranslation } from "react-i18next"
import { Link, useNavigate, useParams } from "react-router-dom"
import { useEffect, useState, useMemo } from "react"
import { useGetUserSkills } from "../../hooks/useGetUserSkills"
import { useGetCategoryByUrl } from "../../hooks/useGetCategoryByUrl"
import { useGetImage } from "../../hooks/useGetImage"
import { useSharedAuth } from "../../api/auth"
import { HttpStatusCode } from "axios"

function ProfileUserCard({ editable, user, inProfileView }: { editable: boolean; user: any; inProfileView: boolean }) {
  const navigate = useNavigate()

  const { t } = useTranslation()
  const { id } = useParams()
  const { userInfo } = useSharedAuth()

  const { getUserSkills } = useGetUserSkills()
  const { getCategoryByUrl } = useGetCategoryByUrl()
  const { getImageByUrl } = useGetImage()

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
            getUserSkills(memorizedUser.id),
            getCategoryByUrl(memorizedUser.links.category),
            getImageByUrl(memorizedUser.links.image),
          ])

          setSkillsData(skillsResponse.status === HttpStatusCode.NoContent ? [] : skillsResponse.data)
          setUserCategory(categoryResponse.data)
          setImageUrl(imageResponse.status === HttpStatusCode.Ok ? memorizedUser.links.image : defaultProfile)
          setLoadingData(false)
        }
      } catch (error) {
        console.error("Error fetching data:", error)
      }
    }
    if (loadingData) fetchData()
  }, [loadingData, memorizedUser])

  const userSkillsList = skillsData.map((skill, index) => {
    return (
      <Badge key={index} pill bg="success" className="mx-2">
        {skill.description}
      </Badge>
    )
  })

  return (
    <Card className="profileCard rounded-3 mx-2" style={{ width: "14rem" }}>
      {userInfo?.role === UserRole.ENTERPRISE ? (
        <Link to={`/users/${user.id}`} style={{ textDecoration: "none", color: "black" }} key={user.id}>
          {imageUrl === "" ? (
            <div className="spinner-border" role="status" />
          ) : (
            <Card.Img variant="top" src={imageUrl} style={{ height: "220px", width: "220px" }} />
          )}
        </Link>
      ) : (
        <>
          <Card.Img variant="top" src={imageUrl} style={{ height: "220px", width: "220px" }} />
          <Button type="button" variant="success" onClick={() => navigate(`image`)}>
            <div className="d-flex align-items-center justify-content-center">
              <Icon.PlusSquare color="white" size={20} style={{ marginRight: "7px" }} />
              {t("Edit Profile Picture")}
            </div>
          </Button>
        </>
      )}
      <Card.Body style={{ alignContent: "left", alignItems: "left" }}>
        <div className="d-flex justify-content-around align-items-center">
          <h5>{user.name}</h5>
          {userInfo?.role === UserRole.USER ? (
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
              {user.links.category !== null ? (
                <div className="d-flex flex-row align-items-center">
                  {t("Category")}:
                  <Badge pill bg="success" className="mx-2" style={{ height: "fit-content" }}>
                    {userCategory!.name == "No-Especificado" ? t("No especificado") : t(userCategory!.name)}
                  </Badge>
                </div>
              ) : (
                <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                  {t("Category")}: {t("No especificado")}
                </p>
              )}
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.Briefcase color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Current Position")}:{" "}
                {user.currentPosition == "" || user.currentPosition == null
                  ? t("No especificado")
                  : user.currentPosition}
              </p>
            </div>
            <div className="d-flex justify-content-start my-2">
              <Icon.Book color="black" size={15} style={{ marginRight: "10px", marginTop: "5px" }} />
              <p style={{ wordBreak: "break-word", textAlign: "left", marginBottom: "0" }}>
                {t("Education Level")}:{" "}
                {user.educationLevel === "" || user.educationLevel == null || user.educationLevel == "No-especificado"
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
  inProfileView: false,
}

export default ProfileUserCard
