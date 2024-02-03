import { fireEvent, render, screen } from "@testing-library/react";
import Login from "../views/login";
import { BrowserRouter } from "react-router-dom";
import MockAdapter from "axios-mock-adapter";
import axios from "axios";

// jest.mock('axios', () => {
//     return {
//       ...(jest.requireActual('axios') as object),
//       create: jest.fn().mockReturnValue(jest.requireActual('axios')),
//     };
//   });
  
// const mockAdapter = new MockAdapter(axios);


describe("Test Login", () => {

    test("Test login page is displayed correctly", async () => {
        render(
            <BrowserRouter>
                <Login />
            </BrowserRouter>
        );
        expect(screen.getByText("Welcome to ClonedIN!")).toBeInTheDocument()
    })

    test("Test login fails with empty credentials", async () => {
        render(
            <BrowserRouter>
                <Login />
            </BrowserRouter>
        );
        const btn = screen.getByTestId("login-button")
        fireEvent.click(btn)
        const errorMessage = await screen.findByText("Invalid Credentials")
        expect(errorMessage).toBeInTheDocument()
    })

    // test("Test login fails with empty credentials", async () => {
    //     render(
    //         <BrowserRouter>
    //             <Login />
    //         </BrowserRouter>
    //     );
    //     const email = screen.getByPlaceholderText("Email")
    //     fireEvent.change(email, {
    //         target: {
    //             value: "paw-2022b-4@gmail.com"
    //         }
    //     })
    //     const password = screen.getByPlaceholderText("Password")
    //     fireEvent.change(password, {
    //         target: {
    //             value: "paw-2022b-4"
    //         }
    //     })
    //     const btn = screen.getByTestId("login-button")
    //     fireEvent.click(btn)
    //     const errorMessage = await screen.findByText("Invalid Credentials")
    //     expect(errorMessage).not.toBeInTheDocument()
    // })

    // test("Test login passes with correct credentials", async () => {
    //     mockAdapter.onPost("/auth/access-token", {
    //         username: "paw-2022b-4@gmail.com",
    //         password: "paw-2022b-4"
    //     }).reply(200)
    //     render(
    //         <BrowserRouter>
    //             <Login />
    //         </BrowserRouter>
    //     );
    //     const email = screen.getByRole("textbox", {
    //         name: /Email/i
    //     })
    //     fireEvent.change(email, {
    //         target: {
    //             value: "paw-2022b-4@gmail.com"
    //         }
    //     })
    //     const password = screen.getByRole("textbox", {
    //         name: /Password/i
    //     })
    //     fireEvent.change(password, {
    //         target: {
    //             value: "paw-2022b-4"
    //         }
    //     })
    //     const btn = screen.getByTestId("login-button")
    //     fireEvent.click(btn)
    // })
});