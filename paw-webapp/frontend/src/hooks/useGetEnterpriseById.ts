import { useRequestApi } from "../api/apiRequest"

export const useGetEnterpriseById = () => {
  const { apiRequest } = useRequestApi()

  async function getEnterpriseById(id: string | undefined) {
    const response = await apiRequest({
      url: `/enterprises/${id}`,
      method: "GET",
      requiresAuth: true,
    })
    return response
  }

  return { getEnterpriseById }
}
