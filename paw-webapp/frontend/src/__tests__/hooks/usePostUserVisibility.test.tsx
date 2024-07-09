import { renderHook, act } from '@testing-library/react';
import { usePostUserVisibility } from '../../hooks/usePostUserVisibility';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('usePostUserVisibility', () => {
  it('updates user visibility successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => usePostUserVisibility ());

    // Mock API request response
    const mockResponse = { status: 200, data: {
        "currentPosition": "Goalie",
        "description": "Goalkeeper for AVFC and La Scaloneta",
        "educationLevel": "Secundario",
        "email": "dibu@argentina.com.ar",
        "id": 28,
        "links": {
            "category": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/categories/10",
            "contacts": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/contacts?userId=28",
            "educations": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/28/educations",
            "experiences": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/28/experiences",
            "image": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/28/image",
            "self": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/28",
            "skills": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/skills?userId=28"
        },
        "location": "London, UK",
        "name": "Emi Martinez",
        "visible": true,
        "yearsOfExperience": 8
    }};
    mockApiRequest.mockResolvedValue(mockResponse);

    const id = "28"
    const name = "Emiliano Martinez"
    const location = "London, UK"
    const currentPosition = "Goalie"
    const description = "Goalkeeper for AVFC and La Scaloneta"
    const category = "ESports"
    const educationLevel = "Secundario"

    const visible = true
   
    let postResult;
    await act(async () => {
      postResult = await result.current.modifyUserVisibility(id, visible);
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
        url: `/users/${id}`,
        method: "POST",
        body: { visible },
    });
    expect(postResult).toEqual(mockResponse);
  });
});