import React from "react"
import { MDBPagination, MDBPaginationItem, MDBPaginationLink } from "mdb-react-ui-kit"
import { useTranslation } from "react-i18next"

export default function Pagination({ setter, pages }: { setter: any; pages: string }) {
  const { t } = useTranslation()
  const paginationNumbers = [];

  for (let i = 1; i <= Number(pages); i++) {
    paginationNumbers.push(i);
  }

  return (
    <nav className="d-flex justify-content-center align-items-center">
      <MDBPagination className="mb-0">
        <MDBPaginationItem onClick={() => setter("1")}>
          <MDBPaginationLink style={{ textDecoration: "none", color: "black" }}>
            {t("Index Pagination First")}
          </MDBPaginationLink>
        </MDBPaginationItem>
        {paginationNumbers.map((pageNumber) => (
          <MDBPaginationItem onClick={() => setter(pageNumber)}>
          <MDBPaginationLink style={{ textDecoration: "none", color: "black" }}>
            {pageNumber}
          </MDBPaginationLink>
        </MDBPaginationItem>
        ))}
        <MDBPaginationItem onClick={() => setter(pages)} >
          <MDBPaginationLink style={{ textDecoration: "none", color: "black" }}>
            {t("Index Pagination End")}
          </MDBPaginationLink>
        </MDBPaginationItem>
      </MDBPagination>
    </nav>
  )
}
