import { useState, type ReactNode } from "react"

type Props = {
    title: string
    isOpen: boolean
    onToggle: () => void
    children: ReactNode
}

export default function Disclosure({ title, isOpen, onToggle, children }: Props) {

    return (
        <section>
            <button
                type="button"
                aria-expanded={isOpen}
                onClick={onToggle}
            >
                <span>{isOpen ? "▾" : "▸"}</span>
                <strong>{title}</strong>
            </button>
            <div hidden={!isOpen} role="region" aria-label={title} style={{ marginTop: 8}}>
                {children}
            </div>
        </section>
    )
}