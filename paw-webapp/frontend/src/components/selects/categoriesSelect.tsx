import "../../styles/App.css"
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.bundle.min"
import Form from "react-bootstrap/Form"
import { useTranslation } from "react-i18next"
import { useState, useEffect } from "react"

function CategoriesSelect() {
  const { t } = useTranslation()

  const [categoryList, setCategoryList] = useState([])
  const [category, setCategory] = useState("")
  const [error, setError] = useState(null)

  useEffect(() => {
    fetch("http://localhost:8080/webapp_war/categories")
      .then((response) => response.json())
      .then((response) => {
        setCategoryList(response)
        setError(null)
      })
      .catch(setError)
  }, [])

  return (
    <div>
      <Form.Select
        className="px-3"
        aria-label="Categories select"
        value={category}
        onChange={(e) => setCategory(e.target.value)}
      >
        <option key="1" value="No-Especificado">
          {t("Category")}
        </option>
        {categoryList.map((categoryListItem: any) => (
          <option key={categoryListItem.id} value={categoryListItem.name}>
            {t(categoryListItem.name)}
          </option>
        ))}
      </Form.Select>
    </div>
  )
}

export default CategoriesSelect
