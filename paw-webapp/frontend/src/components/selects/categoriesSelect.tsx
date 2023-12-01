import "../../styles/App.css"
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.bundle.min"
import Form from "react-bootstrap/Form"
import { useTranslation } from "react-i18next"
import { useState, useEffect } from "react"
import { useRequestApi } from "../../api/apiRequest"

function CategoriesSelect() {
  const { t } = useTranslation()

  const { loading, apiRequest } = useRequestApi()
  const [categoryList, setCategoryList] = useState([])
  const [category, setCategory] = useState("")
  const [error, setError] = useState(null)

  useEffect(() => {
    const fetchCategories = async () => {
      const response = await apiRequest({
        url: "/categories",
        method: "GET",
      })
      setCategoryList(response.data)
      setError(null)
    }

    fetchCategories()
  }, [apiRequest])

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
