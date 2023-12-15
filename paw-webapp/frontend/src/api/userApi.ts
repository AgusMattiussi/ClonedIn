import { useEffect, useState } from "react"
import { useRequestApi } from "./apiRequest"

function GetUserData(uri: any) {
  const { loading, apiRequest } = useRequestApi()
  const [userData, setUserData] = useState<any[]>([])

  // useEffect(() => {
  //   fetch(uri)
  //     .then((response) => response.json())
  //     .then((data) => {
  //       console.log(data)
  //       setUserData(data)
  //     })
  //     .catch((err) => {
  //       console.log(err.message)
  //     })
  // }, [])

  useEffect(() => {
    const fetchUsers = async () => {
      const response = await apiRequest({
        url: uri,
        method: "GET",
      })
      setUserData(response.data)
    }

    fetchUsers()
  }, [apiRequest])

  return userData
}

export default GetUserData
