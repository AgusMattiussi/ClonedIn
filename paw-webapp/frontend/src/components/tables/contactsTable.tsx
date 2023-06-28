import React from "react"
import { useTranslation } from "react-i18next"
import { MDBTable, MDBTableHead, MDBTableBody } from "mdb-react-ui-kit"
import { Button } from "react-bootstrap"

function ContactsTable() {
  const { t } = useTranslation()

  return (
    <MDBTable className="table-light" align="middle">
      <MDBTableHead>
        <tr>
          <th scope="col">{t("Job Offer")}</th>
          <th scope="col">{t("Job Category")}</th>
          <th scope="col">{t("Name")}</th>
          <th scope="col">{t("Category")}</th>
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
          <td>Technology</td>
          <td>
            <a href="/profileUser" style={{ textDecoration: "none" }}>
              Username
            </a>
          </td>
          <td>Technology</td>
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
          <td>Security</td>
          <td>
            <a href="/profileUser" style={{ textDecoration: "none" }}>
              Username
            </a>
          </td>
          <td>Technology</td>
          <td>9/7/1816</td>
          <td>Pendiente</td>
          <td>
            <Button variant="danger" style={{ minWidth: "90px", marginBottom: "5px" }}>
              {t("Cancel")}
            </Button>
            {/* TODO: Add modal */}
          </td>
        </tr>
      </MDBTableBody>
    </MDBTable>
  )
}

export default ContactsTable
