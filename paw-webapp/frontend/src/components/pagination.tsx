import React from "react"
import { MDBPagination, MDBPaginationItem, MDBPaginationLink } from "mdb-react-ui-kit"
import { useTranslation } from "react-i18next"

export default function Pagination({
  setter,
  pages,
  currentPage,
}: {
  setter: any
  pages: string
  currentPage: string
}) {
  const { t } = useTranslation()
  const paginationNumbers = []

  for (let i = 1; i <= Number(pages); i++) {
    paginationNumbers.push(i)
  }

  const handleClick = (newpage: string) => {
    setter(newpage);
  };

  return (
    <nav className="d-flex justify-content-center align-items-center">
      <MDBPagination className="mb-0">
        <MDBPaginationItem disabled={currentPage === "1"} onClick={() => handleClick("1")}>
          <MDBPaginationLink style={{ textDecoration: "none", color: currentPage === "1" ? 'gray' : 'black' }}>
            {t("Index Pagination First")}
          </MDBPaginationLink>
        </MDBPaginationItem>
        {paginationNumbers.map((pageNumber, index) => (
          <MDBPaginationItem
            onClick={() => handleClick(pageNumber.toString())}
            key={index}
          >
            <MDBPaginationLink style={{ textDecoration: "none",
              fontWeight: currentPage === pageNumber.toString() ? 'bold' : 'normal',
            }}>{pageNumber}</MDBPaginationLink>
          </MDBPaginationItem>
        ))}
        <MDBPaginationItem disabled={currentPage === pages} onClick={() => handleClick(pages)}>
          <MDBPaginationLink style={{ textDecoration: "none", color: currentPage === pages ? 'gray' : 'black'}}>
            {t("Index Pagination End")}
          </MDBPaginationLink>
        </MDBPaginationItem>
      </MDBPagination>
    </nav>
  )
}
