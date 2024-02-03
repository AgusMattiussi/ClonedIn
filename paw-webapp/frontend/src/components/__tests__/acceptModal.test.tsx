import { fireEvent, render, screen } from "@testing-library/react";
import AcceptModal from "../modals/acceptModal"

let propsMap: { title: any; msg: any; cancel: any; confirm: any; onConfirmClick?: jest.Mock<any, any>; }

const customPropsMap = (options = {}) => {
    const map = {
        title: "Accept Modal",
        msg: "Accept this modal?",
        cancel: "Cancel",
        confirm: "Confirm",
        onConfirmClick: jest.fn()
    }

    return { ...map, ...options };
};


describe("Test Accept Modal", () => {
    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('Test that modal renders correctly', ()=>{
        render(<AcceptModal {...propsMap}/>)
        expect(screen.getByText(propsMap.title)).toBeInTheDocument()
    })

    test('Test that all props are visible', ()=>{
        render(<AcceptModal {...propsMap}/>)
        expect(screen.getByText(propsMap.title)).toBeInTheDocument()
        expect(screen.getByText(propsMap.msg)).toBeInTheDocument()
        expect(screen.getByText(propsMap.cancel)).toBeInTheDocument()
        expect(screen.getByText(propsMap.confirm)).toBeInTheDocument()
    })

    test('Test that onConfirmClick is run after clicking confirm button', ()=>{
        render(<AcceptModal {...propsMap}/>)
        const button = screen.getByTestId("confirm-button")
        fireEvent.click(button)
        expect(propsMap.onConfirmClick).toHaveBeenCalled()
    })
})