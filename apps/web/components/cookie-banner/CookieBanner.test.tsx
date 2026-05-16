import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import { CookieBanner } from './CookieBanner';

describe('CookieBanner', () => {
  const onAcceptAll = vi.fn();
  const onEssentialOnly = vi.fn();

  beforeEach(() => {
    onAcceptAll.mockReset();
    onEssentialOnly.mockReset();
  });

  function renderBanner() {
    return render(
      <CookieBanner onAcceptAll={onAcceptAll} onEssentialOnly={onEssentialOnly} />,
    );
  }

  it('renders the banner with Accept all and Essentials only buttons', () => {
    renderBanner();
    expect(screen.getByRole('button', { name: /accept all/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /essentials only/i })).toBeInTheDocument();
  });

  it('calls onAcceptAll when Accept all is clicked', () => {
    renderBanner();
    fireEvent.click(screen.getByRole('button', { name: /accept all/i }));
    expect(onAcceptAll).toHaveBeenCalledOnce();
    expect(onEssentialOnly).not.toHaveBeenCalled();
  });

  it('calls onEssentialOnly when Essentials only is clicked', () => {
    renderBanner();
    fireEvent.click(screen.getByRole('button', { name: /essentials only/i }));
    expect(onEssentialOnly).toHaveBeenCalledOnce();
    expect(onAcceptAll).not.toHaveBeenCalled();
  });

  it('opens preferences dialog when Manage is clicked', () => {
    renderBanner();
    fireEvent.click(screen.getByRole('button', { name: /manage/i }));
    expect(screen.getByRole('dialog', { name: /cookie preferences/i })).toBeInTheDocument();
  });

  it('dialog has essential checkbox disabled and always checked', () => {
    renderBanner();
    fireEvent.click(screen.getByRole('button', { name: /manage/i }));
    const essentialCheckbox = screen.getByLabelText(/essential cookies/i);
    expect(essentialCheckbox).toBeChecked();
    expect(essentialCheckbox).toBeDisabled();
  });

  it('can toggle analytics checkbox in dialog', () => {
    renderBanner();
    fireEvent.click(screen.getByRole('button', { name: /manage/i }));
    const analyticsCheckbox = screen.getByLabelText(/analytics cookies/i);
    expect(analyticsCheckbox).not.toBeChecked();
    fireEvent.click(analyticsCheckbox);
    expect(analyticsCheckbox).toBeChecked();
  });

  it('closes dialog when Cancel is clicked', async () => {
    renderBanner();
    fireEvent.click(screen.getByRole('button', { name: /manage/i }));
    expect(screen.getByRole('dialog')).toBeInTheDocument();
    fireEvent.click(screen.getByRole('button', { name: /cancel/i }));
    await waitFor(() => {
      expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
    });
  });

  it('calls onEssentialOnly when save preferences with nothing checked', () => {
    renderBanner();
    fireEvent.click(screen.getByRole('button', { name: /manage/i }));
    fireEvent.click(screen.getByRole('button', { name: /save preferences/i }));
    expect(onEssentialOnly).toHaveBeenCalledOnce();
  });

  it('calls onAcceptAll when analytics is checked and preferences saved', () => {
    renderBanner();
    fireEvent.click(screen.getByRole('button', { name: /manage/i }));
    const analyticsCheckbox = screen.getByLabelText(/analytics cookies/i);
    fireEvent.click(analyticsCheckbox);
    fireEvent.click(screen.getByRole('button', { name: /save preferences/i }));
    expect(onAcceptAll).toHaveBeenCalledOnce();
  });

  it('dialog has aria-modal attribute for accessibility', () => {
    renderBanner();
    fireEvent.click(screen.getByRole('button', { name: /manage/i }));
    const dialog = screen.getByRole('dialog');
    expect(dialog).toHaveAttribute('aria-modal', 'true');
  });

  it('banner has Cookie Policy link', () => {
    renderBanner();
    const link = screen.getByRole('link', { name: /cookie policy/i });
    expect(link).toHaveAttribute('href', '/legal/cookies');
  });
});
