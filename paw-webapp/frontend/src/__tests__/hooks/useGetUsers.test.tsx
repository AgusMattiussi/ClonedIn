import { renderHook, act } from '@testing-library/react';
import { useGetUsers } from '../../hooks/useGetUsers';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('useGetUsers', () => {
  it('fetches users successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => useGetUsers ());

    // Mock API request response
    const mockResponse = { status: 200, data: [
        {
            "currentPosition": "",
            "description": "",
            "educationLevel": "No-especificado",
            "email": "paw@itba.edu.ar",
            "id": 14,
            "links": {
                "category": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/categories/1",
                "contacts": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/contacts?userId=14",
                "educations": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/14/educations",
                "experiences": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/14/experiences",
                "image": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/14/image",
                "self": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/14",
                "skills": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/skills?userId=14"
            },
            "location": "",
            "name": "PAW 1",
            "visible": true,
            "yearsOfExperience": 0
        },
        {
            "currentPosition": "Student",
            "description": "Hi! I'm a student that loves space and programming",
            "educationLevel": "Secundario",
            "email": "paw+1@itba.edu.ar",
            "id": 13,
            "links": {
                "category": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/categories/2",
                "contacts": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/contacts?userId=13",
                "educations": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/13/educations",
                "experiences": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/13/experiences",
                "image": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/13/image",
                "self": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/users/13",
                "skills": "http://old-pawserver.it.itba.edu.ar/paw-2022b-4/api/skills?userId=13"
            },
            "location": "Tigre",
            "name": "PAW 2",
            "visible": true,
            "yearsOfExperience": 2
        }
    ] };
    mockApiRequest.mockResolvedValue(mockResponse);

    let categories;
    await act(async () => {
      categories = await result.current.getUsers();
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
        url: `/users`,
        method: "GET",
        queryParams: {},
        requiresAuth: true,
    });
    expect(categories).toEqual(mockResponse);
  });
});