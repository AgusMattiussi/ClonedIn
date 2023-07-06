import React from "react"
import { MDBPagination, MDBPaginationItem, MDBPaginationLink } from "mdb-react-ui-kit"
import { useTranslation } from "react-i18next"

export default function Pagination({ path, currentPage, pages }: { path: string; currentPage: string; pages: string }) {
  const { t } = useTranslation()

  return (
    <nav className="d-flex justify-content-center align-items-center">
      <MDBPagination className="mb-0">
        <MDBPaginationItem disabled>
          <MDBPaginationLink href="#" tabIndex={-1} aria-disabled="true">
            {t("Index Pagination First")}
          </MDBPaginationLink>
        </MDBPaginationItem>
        <MDBPaginationItem>
          <MDBPaginationLink href="#" style={{ textDecoration: "none", color: "black", fontWeight: "bold" }}>
            1
          </MDBPaginationLink>
        </MDBPaginationItem>
        <MDBPaginationItem>
          <MDBPaginationLink href="#" style={{ textDecoration: "none", color: "black" }}>
            {t("Index Pagination End")}
          </MDBPaginationLink>
        </MDBPaginationItem>
      </MDBPagination>
    </nav>
  )
}

Pagination.defaultProps = {
  path: "#",
  currentPage: "#",
  pages: "#",
}
