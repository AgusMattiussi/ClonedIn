import { useRequestApi } from "../api/apiRequest"

export const useGetJobOfferData = () => {
  const { apiRequest } = useRequestApi()

  async function getJobOffers(queryParams: Record<string, string> = {}) {
    const response = await apiRequest({
      url: `/jobOffers`,
      method: "GET",
      queryParams: queryParams,
    })
    return response
  }

  async function getJobOfferById(id: string | undefined) {
    const response = await apiRequest({
      url: `/jobOffers/${id}`,
      method: "GET",
    })
    return response
  }

  async function getJobOfferByUrl(jobOfferUrl: string) {
    const response = await apiRequest({
      url: jobOfferUrl,
      method: "GET",
    })
    return response
  }

  async function getUserJobs(id: string | undefined, queryParams: Record<string, string> = {}) {
    const response = await apiRequest({
      url: `/jobOffers?enterpriseId=${id}`,
      method: "GET",
      queryParams: queryParams,
    })
    return response
  }

  return { getJobOffers, getJobOfferById, getJobOfferByUrl, getUserJobs }
}
