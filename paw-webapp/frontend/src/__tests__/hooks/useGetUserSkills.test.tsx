import { renderHook, act } from '@testing-library/react';
import { useGetUserSkills } from '../../hooks/useGetUserSkills';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('useGetUserSkills', () => {
  it('fetches users skills successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => useGetUserSkills ());

    // Mock API request response
    const mockResponse = { status: 200, data: [
        {
            "description": "c",
            "id": 2,
            "links": {
                "jobOffersWithSkill": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/jobOffers?skillId=2",
                "self": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/skills/2",
                "usersWithSkill": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users?skillId=2"
            }
        },
        {
            "description": "java",
            "id": 3,
            "links": {
                "jobOffersWithSkill": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/jobOffers?skillId=3",
                "self": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/skills/3",
                "usersWithSkill": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users?skillId=3"
            }
        }
    ] };
    mockApiRequest.mockResolvedValue(mockResponse);

    const id = "28"

    let categories;
    await act(async () => {
      categories = await result.current.getUserSkills(id);
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
        url: `/users/${id}/skills`,
        method: "GET",
        requiresAuth: true,
    });
    expect(categories).toEqual(mockResponse);
  });
});