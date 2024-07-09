import { renderHook, act } from '@testing-library/react';
import { usePostJobOffer } from '../../hooks/usePostJobOffer';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('usePostJobOffer', () => {
  it('creates job offer successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => usePostJobOffer());

    // Mock API request response
    const mockResponse = { status: 201, data: { message: 'Job offer created successfully' } };
    mockApiRequest.mockResolvedValue(mockResponse);

    const id = "28"
    const position = "QA Tester"
    const description = "Only the best testers are welcome."
    const salary = "120000"
    const category = "Business"
    const modality =  "Mixed"
    const skill1 =  "Testing"
    const skill2 = "Mockito"
    const skill3 = "Express"
    const skill4 = "Javascript"
   
    let postResult;
    await act(async () => {
      postResult = await result.current.addJobOffer(id, position, description, salary, category, modality, skill1, skill2, skill3, skill4);
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
        url: `/jobOffers`,
        method: "POST",
        body: {
          position,
          description,
          salary,
          category,
          modality,
          skill1,
          skill2,
          skill3,
          skill4,
        },
    });
    expect(postResult).toEqual(mockResponse);
  });

  it('fails to create job offer', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => usePostJobOffer());

    // Mock API request response
    const mockResponse = { status: 400, data: [
        {
            "detail": "may not be empty",
            "errorClass": "ConstraintViolationException",
            "message": "Invalid parameter: position = ."
        }
    ] };
    mockApiRequest.mockResolvedValue(mockResponse);

    const id = "28"
    const position = ""
    const description = "Only the best testers are welcome."
    const salary = "120000"
    const category = "Business"
    const modality =  "Mixed"
    const skill1 =  "Testing"
    const skill2 = "Mockito"
    const skill3 = "Express"
    const skill4 = "Javascript"
   
    let postResult;
    await act(async () => {
      postResult = await result.current.addJobOffer(id, position, description, salary, category, modality, skill1, skill2, skill3, skill4);
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
        url: `/jobOffers`,
        method: "POST",
        body: {
          position,
          description,
          salary,
          category,
          modality,
          skill1,
          skill2,
          skill3,
          skill4,
        },
    });
    expect(postResult).toEqual(mockResponse);
  });
});