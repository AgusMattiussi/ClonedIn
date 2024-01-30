import { useRequestApi } from "../api/apiRequest"

export const usePostEnterpriseData = () => {
  const { apiRequest } = useRequestApi()

  async function addJobOffer(
    id: string | undefined,
    jobPosition: string,
    jobDescription: string,
    salary: string,
    category: string,
    modality: string,
    skill1: string,
    skill2: string,
    skill3: string,
    skill4: string,
  ) {
    const response = await apiRequest({
      url: `/enterprises/${id}/jobOffers`,
      method: "POST",
      body: {
        jobPosition,
        jobDescription,
        salary,
        category,
        modality,
        skill1,
        skill2,
        skill3,
        skill4,
      },
    })
    return response
  }

  async function addContact(id: string | undefined, message: string, jobOfferId: string, userId: string | undefined) {
    const response = await apiRequest({
      url: `/enterprises/${id}/contacts`,
      method: "POST",
      body: {
        message,
        jobOfferId,
        userId,
      },
    })
    return response
  }

  return { addJobOffer, addContact }
}
