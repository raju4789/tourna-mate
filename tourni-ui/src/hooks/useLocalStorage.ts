const useLocalStorage = (key:string) => {
  const setItem = (value: unknown) => {
    try {
      // Store strings directly, JSON.stringify for objects/arrays
      if (typeof value === 'string') {
        localStorage.setItem(key, value);
      } else {
        localStorage.setItem(key, JSON.stringify(value));
      }
    } catch (error) {
      console.error('Error saving to local storage', error);
    }
  };

  const getItem = () => {
    try {
      const item = localStorage.getItem(key);
      if (!item) return null;
      
      // Try to parse as JSON, if it fails, return as string
      try {
        return JSON.parse(item);
      } catch {
        // If JSON.parse fails, it's a plain string (like JWT token)
        return item;
      }
    } catch (error) {
      console.error('Error reading from local storage', error);
      return null;
    }
  };

  const removeItem = () => {
    try {
      localStorage.removeItem(key);
    } catch (error) {
      console.error('Error removing from local storage', error);
    }
  };

  const clear = () => {
    try {
      localStorage.clear();
    } catch (error) {
      console.error('Error clearing local storage', error);
    }
  };

  return {
    setItem, getItem, removeItem, clear,
  };
};

export default useLocalStorage;
