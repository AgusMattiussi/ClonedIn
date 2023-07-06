import React from "react"
import Badge from "react-bootstrap/Badge"
import { useTranslation } from "react-i18next"
import { MDBTable, MDBTableHead, MDBTableBody } from "mdb-react-ui-kit"
import { Button } from "react-bootstrap"
import AcceptModal from "../modals/acceptModal"
import RejectModal from "../modals/rejectModal"

function InterestedTable() {
  const { t } = useTranslation()

  return (
    <MDBTable
      className="table-light"
      align="middle"
      style={{ boxShadow: "0 0 2px rgba(0,0,0,0.16), 0 0 1px rgba(0,0,0,0.23)" }}
    >
      <MDBTableHead>
        <tr>
          <th scope="col">{t("Job Offer")}</th>
          <th scope="col">{t("Name")}</th>
          <th scope="col">{t("Category")}</th>
          <th scope="col">{t("Skills")}</th>
          <th scope="col">{t("Years Of Experience")}</th>
          <th scope="col">{t("Date")}</th>
          <th scope="col">{t("Status")}</th>
          <th />
        </tr>
      </MDBTableHead>
      <MDBTableBody>
        <tr>
          <td>
            <a href="/jobOffer" style={{ textDecoration: "none" }}>
              CEO
            </a>
          </td>
          <td>
            <a href="/profileUser" style={{ textDecoration: "none" }}>
              Username
            </a>
          </td>
          <td>Technology</td>
          <td>
            <div className="d-flex flex-column align-items-center">
              <Badge pill bg="success" style={{ marginBottom: "0.5rem", width: "fit-content" }}>
                skill1
              </Badge>
              <Badge pill bg="success" style={{ marginBottom: "0.5rem", width: "fit-content" }}>
                skill2
              </Badge>
              <Badge pill bg="success" style={{ marginBottom: "0.5rem", width: "fit-content" }}>
                skill3
              </Badge>
            </div>
          </td>
          <td>4</td>
          <td>25/5/1810</td>
          <td>Aceptada</td>
          <td />
        </tr>
        <tr>
          <td>
            <a href="/jobOffers" style={{ textDecoration: "none" }}>
              CSO
            </a>
          </td>
          <td>
            <a href="/profileUser" style={{ textDecoration: "none" }}>
              Username
            </a>
          </td>
          <td>Security</td>
          <td>
            <div className="d-flex flex-column align-items-center">
              <Badge pill bg="success" style={{ marginBottom: "0.5rem", width: "fit-content" }}>
                skill1
              </Badge>
              <Badge pill bg="success" style={{ marginBottom: "0.5rem", width: "fit-content" }}>
                skill2
              </Badge>
            </div>
          </td>
          <td>2</td>
          <td>9/7/1816</td>
          <td>Pendiente</td>
          <td>
            <div className="d-flex flex-column">
              <Button
                variant="success"
                style={{ minWidth: "90px", marginBottom: "5px" }}
                data-bs-toggle="modal"
                data-bs-target="#acceptModal"
              >
                {t("Accept")}
              </Button>
              <AcceptModal
                title={t("Modal Title")}
                msg={t("Accept Application Modal Msg")}
                cancel={t("Cancel")}
                confirm={t("Confirm")}
              />
              <Button
                variant="danger"
                style={{ minWidth: "90px", marginBottom: "5px" }}
                data-bs-toggle="modal"
                data-bs-target="#rejectModal"
              >
                {t("Decline")}
              </Button>
              <RejectModal
                title={t("Modal Title")}
                msg={t("Reject Application Modal Msg")}
                cancel={t("Cancel")}
                confirm={t("Confirm")}
              />
            </div>
          </td>
        </tr>
      </MDBTableBody>
    </MDBTable>
  )
}

export default InterestedTable
