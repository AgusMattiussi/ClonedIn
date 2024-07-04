import React, { useState } from "react"
import { MDBPagination, MDBPaginationItem, MDBPaginationLink } from "mdb-react-ui-kit"
import { useTranslation } from "react-i18next"

export default function Pagination({ setter, pages }: { setter: any; pages: string }) {
  const { t } = useTranslation()
  const paginationNumbers = []
  const [currentPage, setCurrentPage] = useState("1")

  for (let i = 1; i <= Number(pages); i++) {
    paginationNumbers.push(i)
  }

  const handleClick = (newpage: string) => {
    setCurrentPage(newpage)
    setter(newpage)
  }

  return (
    <nav className="d-flex justify-content-center align-items-center" style={{ marginBottom: "10px" }}>
      <MDBPagination className="mb-0">
        <MDBPaginationItem disabled={currentPage === "1"} onClick={() => handleClick("1")}>
          <MDBPaginationLink style={{ textDecoration: "none", color: currentPage === "1" ? "grey" : "black" }}>
            {t("Index Pagination First")}
          </MDBPaginationLink>
        </MDBPaginationItem>
        {paginationNumbers.map((pageNumber, index) => (
          <MDBPaginationItem onClick={() => handleClick(pageNumber.toString())} key={index} style={{ color: "black" }}>
            <MDBPaginationLink
              style={{ fontWeight: currentPage === pageNumber.toString() ? "bold" : "normal", color: "black" }}
            >
              {pageNumber}
            </MDBPaginationLink>
          </MDBPaginationItem>
        ))}
        <MDBPaginationItem disabled={currentPage === pages} onClick={() => handleClick(pages)}>
          <MDBPaginationLink style={{ textDecoration: "none", color: currentPage === pages ? "gray" : "black" }}>
            {t("Index Pagination End")}
          </MDBPaginationLink>
        </MDBPaginationItem>
      </MDBPagination>
    </nav>
  )
}
