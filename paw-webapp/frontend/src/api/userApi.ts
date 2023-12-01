import { useEffect, useState } from "react"

function GetUserData(uri: any) {
  const [userData, setUserData] = useState<any[]>([])

  useEffect(() => {
    fetch(uri)
      .then((response) => response.json())
      .then((data) => {
        console.log(data)
        setUserData(data)
      })
      .catch((err) => {
        console.log(err.message)
      })
  }, [])

  return userData
}

export default GetUserData
