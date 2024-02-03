import { useRequestApi } from "../api/apiRequest"

export const useGetEnterpriseByUrl = () => {
  const { apiRequest } = useRequestApi()

  async function getEnterpriseByUrl(enterpriseUrl: string) {
    const response = await apiRequest({
      url: enterpriseUrl,
      method: "GET",
      requiresAuth: true,
    })
    return response
  }

  return { getEnterpriseByUrl }
}
