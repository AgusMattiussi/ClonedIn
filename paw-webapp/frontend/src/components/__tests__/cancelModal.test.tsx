import { fireEvent, render, screen } from "@testing-library/react";
import CancelModal from "../modals/cancelModal";

let propsMap: { title: any; msg: any; cancel: any; confirm: any; onConfirmClick?: jest.Mock<any, any>; }

const customPropsMap = (options = {}) => {
    const map = {
        title: "Cancel Modal",
        msg: "Cancel this modal?",
        cancel: "Cancel",
        confirm: "Confirm",
        onConfirmClick: jest.fn()
    }

    return { ...map, ...options };
};


describe("Test Cancel Modal", () => {
    beforeEach(()=>{
        propsMap = customPropsMap()
    })

    test('Test that modal renders correctly', ()=>{
        render(<CancelModal {...propsMap}/>)
        expect(screen.getByText(propsMap.title)).toBeInTheDocument()
    })

    test('Test that all props are visible', ()=>{
        render(<CancelModal {...propsMap}/>)
        expect(screen.getByText(propsMap.title)).toBeInTheDocument()
        expect(screen.getByText(propsMap.msg)).toBeInTheDocument()
        expect(screen.getByText(propsMap.cancel)).toBeInTheDocument()
        expect(screen.getByText(propsMap.confirm)).toBeInTheDocument()
    })

    test('Test that onConfirmClick is run after clicking confirm button', ()=>{
        render(<CancelModal {...propsMap}/>)
        const button = screen.getByTestId("confirm-button")
        fireEvent.click(button)
        expect(propsMap.onConfirmClick).toHaveBeenCalled()
    })
})