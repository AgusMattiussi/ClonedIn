// import MockAdapter from "axios-mock-adapter"
// import { useRequestApi } from "../../api/apiRequest"
// import axios from "axios"
// import { renderHook } from "@testing-library/react"
// import { useGetCategories } from "../useGetCategories"

// describe("Test useGetCategories", () => {
//     beforeEach(() => {
//         jest.clearAllMocks()
//     })
//     test("getCategories", async() => {
//         const categories = [
//             {
//                 "id":2,
//                 "links":{
//                    "enterprisesInCategory":"http://localhost:8080/api/enterprises?categoryName=Tecnologia",
//                    "jobOffersInCategory":"http://localhost:8080/api/jobOffers?categoryName=Tecnologia",
//                    "self":"http://localhost:8080/api/categories/2",
//                    "usersInCategory":"http://localhost:8080/api/users?categoryName=Tecnologia"
//                 },
//                 "name":"Tecnologia"
//              },
//              {
//                 "id":3,
//                 "links":{
//                    "enterprisesInCategory":"http://localhost:8080/api/enterprises?categoryName=Medicina",
//                    "jobOffersInCategory":"http://localhost:8080/api/jobOffers?categoryName=Medicina",
//                    "self":"http://localhost:8080/api/categories/3",
//                    "usersInCategory":"http://localhost:8080/api/users?categoryName=Medicina"
//                 },
//                 "name":"Medicina"
//              },
//              {
//                 "id":4,
//                 "links":{
//                    "enterprisesInCategory":"http://localhost:8080/api/enterprises?categoryName=Recursos-Humanos",
//                    "jobOffersInCategory":"http://localhost:8080/api/jobOffers?categoryName=Recursos-Humanos",
//                    "self":"http://localhost:8080/api/categories/4",
//                    "usersInCategory":"http://localhost:8080/api/users?categoryName=Recursos-Humanos"
//                 },
//                 "name":"Recursos-Humanos"
//              },
//         ]
//         const response = {
//             status: 200,
//             data: categories
//         }
//         jest.spyOn(useGetCategories.prototype, "getCategories").mockReturnValue(200)
//         const { getCategories } = useGetCategories()
//         const resp = await getCategories()
//         expect(resp).toEqual(200) 
//     })
// })