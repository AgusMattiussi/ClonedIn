import { useRequestApi } from "../api/apiRequest"

export const usePostContact = () => {
  const { apiRequest } = useRequestApi()

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

  return { addContact }
}
